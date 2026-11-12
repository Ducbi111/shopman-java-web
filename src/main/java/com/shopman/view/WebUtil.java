package com.shopman.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class WebUtil {
    private WebUtil() {}
    public static int positiveInt(String value,String field){
        try{int n=Integer.parseInt(value);if(n<=0)throw new NumberFormatException();return n;}
        catch(NumberFormatException e){throw new IllegalArgumentException(field+" phải là số nguyên dương");}
    }
    public static long positiveLong(String value,String field){
        try{long n=Long.parseLong(value);if(n<=0)throw new NumberFormatException();return n;}
        catch(NumberFormatException e){throw new IllegalArgumentException(field+" không hợp lệ");}
    }
    public static void flash(HttpSession session,String type,String message){session.setAttribute("flashType",type);session.setAttribute("flash",message);}
    public static void exposeFlash(HttpServletRequest request){
        HttpSession s=request.getSession();request.setAttribute("flash",s.getAttribute("flash"));request.setAttribute("flashType",s.getAttribute("flashType"));
        s.removeAttribute("flash");s.removeAttribute("flashType");
    }
}
