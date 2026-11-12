package com.shopman.view.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/customer/order-success")
public class OrderSuccessView8 extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        Object code=req.getSession().getAttribute("orderCode");if(code==null){resp.sendRedirect(req.getContextPath()+"/customer/products");return;}
        req.setAttribute("orderCode",code);req.getSession().removeAttribute("orderCode");req.getRequestDispatcher("/WEB-INF/views/customer/order-success.jsp").forward(req,resp);
    }
}
