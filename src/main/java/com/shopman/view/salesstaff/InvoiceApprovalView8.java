package com.shopman.view.salesstaff;

import com.shopman.dao.InvoiceDAO8;
import com.shopman.model.UserSession;
import com.shopman.view.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/sales/invoices")
public class InvoiceApprovalView8 extends HttpServlet {
    private final InvoiceDAO8 dao=new InvoiceDAO8();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        try{req.setAttribute("invoices",dao.findPending());WebUtil.exposeFlash(req);req.getRequestDispatcher("/WEB-INF/views/sales/invoices.jsp").forward(req,resp);}
        catch(SQLException e){throw new ServletException(e);}
    }
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        try{long id=WebUtil.positiveLong(req.getParameter("invoiceId"),"Hóa đơn");long staff=((UserSession)req.getSession().getAttribute("user")).getMemberId();dao.approve(id,staff);WebUtil.flash(req.getSession(),"success","Đã duyệt hóa đơn và chuyển sang đóng hàng");}
        catch(IllegalArgumentException e){WebUtil.flash(req.getSession(),"error",e.getMessage());}
        catch(SQLException e){throw new ServletException(e);}
        resp.sendRedirect(req.getContextPath()+"/sales/invoices");
    }
}
