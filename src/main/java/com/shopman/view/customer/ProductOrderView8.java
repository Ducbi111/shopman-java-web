package com.shopman.view.customer;

import com.shopman.dao.CartDAO8;
import com.shopman.dao.ProductDAO8;
import com.shopman.model.UserSession;
import com.shopman.view.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/customer/products")
public class ProductOrderView8 extends HttpServlet {
    private final ProductDAO8 productDAO=new ProductDAO8(); private final CartDAO8 cartDAO=new CartDAO8();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        try{
            String keyword=req.getParameter("keyword");req.setAttribute("keyword",keyword==null?"":keyword);
            req.setAttribute("products",productDAO.searchByName(keyword));
            UserSession u=(UserSession)req.getSession().getAttribute("user");req.setAttribute("cart",cartDAO.findOrCreate(u.getMemberId()));
            WebUtil.exposeFlash(req);req.getRequestDispatcher("/WEB-INF/views/customer/products.jsp").forward(req,resp);
        }catch(SQLException e){throw new ServletException("Không thể tìm mặt hàng",e);}
    }
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        UserSession u=(UserSession)req.getSession().getAttribute("user");
        try{
            long productId=WebUtil.positiveLong(req.getParameter("productId"),"Mặt hàng");
            int quantity=WebUtil.positiveInt(req.getParameter("quantity"),"Số lượng");
            cartDAO.addOrUpdateItem(u.getMemberId(),productId,quantity);WebUtil.flash(req.getSession(),"success","Đã thêm mặt hàng vào giỏ");
        }catch(IllegalArgumentException e){WebUtil.flash(req.getSession(),"error",e.getMessage());}
        catch(SQLException e){throw new ServletException("Không thể cập nhật giỏ hàng",e);}
        String keyword=req.getParameter("keyword");resp.sendRedirect(req.getContextPath()+"/customer/products?keyword="+java.net.URLEncoder.encode(keyword==null?"":keyword,java.nio.charset.StandardCharsets.UTF_8));
    }
}
