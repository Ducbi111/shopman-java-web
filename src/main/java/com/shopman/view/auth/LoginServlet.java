package com.shopman.view.auth;

import com.shopman.dao.MemberDAO8;
import com.shopman.model.UserSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final MemberDAO8 dao=new MemberDAO8();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        if(req.getSession().getAttribute("user")!=null){redirectByRole(req,resp,(UserSession)req.getSession().getAttribute("user"));return;}
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req,resp);
    }
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        try{
            var user=dao.authenticate(req.getParameter("username"),req.getParameter("password"));
            if(user.isEmpty()){req.setAttribute("error","Tên đăng nhập hoặc mật khẩu không đúng");doGet(req,resp);return;}
            req.getSession().invalidate();HttpSession session=req.getSession(true);session.setAttribute("user",user.get());redirectByRole(req,resp,user.get());
        }catch(SQLException e){throw new ServletException("Không thể đăng nhập",e);}
    }
    private void redirectByRole(HttpServletRequest req,HttpServletResponse resp,UserSession u)throws IOException{
        String home=switch(u.getRole()){case "MANAGER"->"/manager/products";case "CUSTOMER"->"/customer/products";case "SALES"->"/sales/invoices";default->"/login";};
        resp.sendRedirect(req.getContextPath()+home);
    }
}
