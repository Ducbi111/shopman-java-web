package com.shopman.view.customer;

import com.shopman.dao.InvoiceDAO8;
import com.shopman.model.UserSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/customer/orders")
public class CustomerOrdersView8 extends HttpServlet {
    private final InvoiceDAO8 dao=new InvoiceDAO8();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        try{req.setAttribute("invoices",dao.findByCustomer(((UserSession)req.getSession().getAttribute("user")).getMemberId()));req.getRequestDispatcher("/WEB-INF/views/customer/orders.jsp").forward(req,resp);}
        catch(SQLException e){throw new ServletException("Không thể tải hóa đơn",e);}
    }
}
