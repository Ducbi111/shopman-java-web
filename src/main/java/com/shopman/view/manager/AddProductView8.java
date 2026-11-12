package com.shopman.view.manager;

import com.shopman.dao.ProductDAO8;
import com.shopman.model.Product8;
import com.shopman.model.UserSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/manager/products/add")
public class AddProductView8 extends HttpServlet {
    private final ProductDAO8 dao=new ProductDAO8();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.getRequestDispatcher("/WEB-INF/views/manager/add-product.jsp").forward(req,resp);
    }
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        Product8 p=new Product8();List<String> errors=new ArrayList<>();
        p.setProductCode(trim(req,"productCode"));p.setName(trim(req,"name"));p.setProductType(trim(req,"productType"));p.setUnit(trim(req,"unit"));
        p.setDescription(trim(req,"description"));p.setImage(trim(req,"image"));
        if(p.getProductCode().isBlank())errors.add("Mã mặt hàng là bắt buộc");if(p.getName().isBlank())errors.add("Tên mặt hàng là bắt buộc");
        if(p.getProductType().isBlank())errors.add("Loại hàng là bắt buộc");if(p.getUnit().isBlank())errors.add("Đơn vị tính là bắt buộc");
        try{p.setPrice(new BigDecimal(req.getParameter("price")));if(p.getPrice().signum()<0)throw new NumberFormatException();}catch(Exception e){errors.add("Giá bán phải là số không âm");}
        try{p.setStockQuantity(Integer.valueOf(req.getParameter("stockQuantity")));if(p.getStockQuantity()<0)throw new NumberFormatException();}catch(Exception e){errors.add("Số lượng tồn phải là số nguyên không âm");}
        p.setCreatedByManagerId(((UserSession)req.getSession().getAttribute("user")).getMemberId());
        try{if(errors.isEmpty()&&dao.existsByCodeOrName(p.getProductCode(),p.getName()))errors.add("Mã hoặc tên mặt hàng đã tồn tại");}
        catch(SQLException e){throw new ServletException(e);}
        if(!errors.isEmpty()){req.setAttribute("errors",errors);req.setAttribute("product",p);doGet(req,resp);return;}
        try{dao.insert(p);req.getSession().setAttribute("createdProduct",p);resp.sendRedirect(req.getContextPath()+"/manager/products/add-success");}
        catch(SQLException e){throw new ServletException("Không thể lưu mặt hàng",e);}
    }
    private String trim(HttpServletRequest r,String n){String v=r.getParameter(n);return v==null?"":v.trim();}
}
