package com.shopman.view.customer;

import com.shopman.dao.CartDAO8;
import com.shopman.model.UserSession;
import com.shopman.service.OrderService;
import com.shopman.view.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/customer/cart")
public class CartView8 extends HttpServlet {
    private final CartDAO8 cartDAO=new CartDAO8();private final OrderService orderService=new OrderService();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        UserSession u=(UserSession)req.getSession().getAttribute("user");
        try{req.setAttribute("cart",cartDAO.findOrCreate(u.getMemberId()));req.setAttribute("defaultAddress",u.getShippingAddress());WebUtil.exposeFlash(req);req.getRequestDispatcher("/WEB-INF/views/customer/cart.jsp").forward(req,resp);}
        catch(SQLException e){throw new ServletException("Không thể tải giỏ hàng",e);}
    }
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        UserSession u=(UserSession)req.getSession().getAttribute("user");String action=req.getParameter("action");
        try{
            if("update".equals(action)){
                long itemId=WebUtil.positiveLong(req.getParameter("itemId"),"Dòng giỏ hàng");int quantity=Integer.parseInt(req.getParameter("quantity"));
                cartDAO.updateQuantity(u.getMemberId(),itemId,quantity);WebUtil.flash(req.getSession(),"success",quantity<=0?"Đã xóa mặt hàng":"Đã cập nhật số lượng");
            }else if("checkout".equals(action)){
                String code=orderService.createPendingInvoice(u.getMemberId(),req.getParameter("shippingAddress"));
                req.getSession().setAttribute("orderCode",code);resp.sendRedirect(req.getContextPath()+"/customer/order-success");return;
            }
        }catch(IllegalArgumentException e){WebUtil.flash(req.getSession(),"error",e.getMessage());}
        catch(SQLException e){throw new ServletException("Không thể xử lý giỏ hàng",e);}
        resp.sendRedirect(req.getContextPath()+"/customer/cart");
    }
}
