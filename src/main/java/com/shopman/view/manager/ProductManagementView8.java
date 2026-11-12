package com.shopman.view.manager;

import com.shopman.dao.ProductDAO8;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/manager/products")
public class ProductManagementView8 extends HttpServlet {
    private final ProductDAO8 dao=new ProductDAO8();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        try{req.setAttribute("products",dao.findAll());req.getRequestDispatcher("/WEB-INF/views/manager/products.jsp").forward(req,resp);}
        catch(SQLException e){throw new ServletException("Không thể tải danh sách mặt hàng",e);}
    }
}
