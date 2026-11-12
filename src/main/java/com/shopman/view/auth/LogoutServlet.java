package com.shopman.view.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException{
        req.getSession().invalidate();resp.sendRedirect(req.getContextPath()+"/login");
    }
}
