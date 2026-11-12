package com.shopman.filter;

import com.shopman.model.UserSession;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns={"/manager/*","/customer/*","/sales/*"})
public class AuthenticationFilter implements Filter {
    @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)req; HttpServletResponse response=(HttpServletResponse)res;
        UserSession user=(UserSession)request.getSession().getAttribute("user");
        if(user==null){response.sendRedirect(request.getContextPath()+"/login");return;}
        String path=request.getServletPath();
        boolean allowed=(path.startsWith("/manager/")&&"MANAGER".equals(user.getRole()))
                ||(path.startsWith("/customer/")&&"CUSTOMER".equals(user.getRole()))
                ||(path.startsWith("/sales/")&&"SALES".equals(user.getRole()));
        if(!allowed){response.sendError(HttpServletResponse.SC_FORBIDDEN,"Bạn không có quyền truy cập chức năng này");return;}
        chain.doFilter(req,res);
    }
}
