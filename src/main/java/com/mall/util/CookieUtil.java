package com.mall.util;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieUtil {//get和set分别是从cookie里面读取数据和写入数据。URLDecoder和URLEncoder用来解决写入cookie出错的问题
    public static String getCookie(HttpServletRequest request,
                                   String cookieName){
        Cookie[] cookies = request.getCookies();//request是以数组形式返回的
        if (cookies != null){
            for(Cookie cookie : cookies){
                if (cookie.getName().equals(cookieName)){
                    try{
                        return URLDecoder.decode(cookie.getValue(),
                                "utf-8");
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
    public static void writeCookie(HttpServletResponse response,
                                   String cookieName, String value){
        try {
            value = URLEncoder.encode(value, "utf-8");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }
}
