package com.neuedu.utils;



import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 封装cookie工具类
 * */
public class CookieUtils {

    private static final  String  COOKIE_DOMAIN="ilearn.com";

    /**
     * 添加cookie
     *
     * ilearn.com/
     * ilearn.com/test/a
     * ilearn.com/test/b
     * user.ilearn.com
     * img.ilearn.com
     *
     * */

    public  static  void  writeCookie(HttpServletResponse response,String cookeName,String cookieValue){

        Cookie cookie=new Cookie(cookeName,cookieValue);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60*24*7);
        response.addCookie(cookie);

    }

  /**
   * 读取cookie
   * */
  public   static  String   readCookie(HttpServletRequest request,String cookieName){

      Cookie[] cookies=request.getCookies();
      if(cookies!=null&&cookies.length>0){
          for(Cookie c:cookies){
              if(c.getName().equals(cookieName)){
                  return c.getValue();
              }
          }
      }
      return null;
  }

  /**
   * 删除cookie
   * */
  public  static  void  deleteCookie(HttpServletRequest request, HttpServletResponse response,String cookieName){

      Cookie[] cookies=request.getCookies();
      if(cookies!=null&&cookies.length>0){
          for(Cookie c:cookies){
              if(c.getName().equals(cookieName)){
                c.setDomain(COOKIE_DOMAIN);
                c.setPath("/");
                c.setMaxAge(0);
                response.addCookie(c);
              }
          }
      }
  }


}
