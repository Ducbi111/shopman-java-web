package com.shopman.view.manager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/manager/products/add-success")
public class AddProductSuccessView8 extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        if(req.getSession().getAttribute("createdProduct")==null){resp.sendRedirect(req.getContextPath()+"/manager/products");return;}
        req.getRequestDispatcher("/WEB-INF/views/manager/add-success.jsp").forward(req,resp);
    }
}
