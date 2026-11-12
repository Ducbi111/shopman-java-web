package com.shopman.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class EncodingFilter implements Filter {
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8"); response.setCharacterEncoding("UTF-8");
        // Không ép Content-Type ở filter: tài nguyên tĩnh như CSS/JS phải để
        // Tomcat tự gán đúng MIME (text/css, application/javascript, ...).
        chain.doFilter(request,response);
    }
}
