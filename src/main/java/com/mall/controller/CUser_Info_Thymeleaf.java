package com.mall.controller;

import com.mall.entity.User_info;
import com.mall.service.User_infoService;
import com.mall.util.CookieUtil;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CUser_Info_Thymeleaf {
    @Autowired
    private User_infoService user_infoService;

    //通过手机号登录
    @RequestMapping("login")
    public String showLogin(){
        return "login";
    }
    @RequestMapping("logout")
    public String loginout(Model model, HttpServletRequest request){
        request.getSession().removeAttribute("user_name");
        request.getSession().removeAttribute("user_id");
        //System.out.println(request.getSession().getAttribute("user_name"));
        return "redirect:login";
    }
    @RequestMapping("index")//返回主页面
    public String showIndex(Model model, HttpServletRequest request){
        //index页面分成登录和没登录2种情况。没登录商品存cookie，登录存redis。
         return "index";
    }
    @RequestMapping("checklogin")
    public String showLogin(HttpServletRequest request){
        String telephone = request.getParameter("phone").toString();
        String pwd = request.getParameter("pnum").toString();//login.html页面叫的是pnum这个名字。
        //检测手机号和密码登录，如果成功，则返回用户的信息，至少应该有用户的主键id--user_id给session保存起来。
        User_info user_info = user_infoService.checkLoginMultiParam(telephone, pwd);
        //登录成功返回主界面，如果不成功则重新登录。
        if (user_info == null)
            return "login";
        else {//验证成功则把用户id和姓名存入session。
            request.getSession().setAttribute("user_id", user_info.getUser_id());
            request.getSession().setAttribute("user_name", user_info.getUser_name());
            return "redirect:index";
        }
    }
    /*
    @RequestMapping(value = "check_login", method = RequestMethod.POST)
    public String checkLogin(HttpServletRequest request, HttpServletResponse response){
        User_info user_info = new User_info();
        user_info.setUser_name(request.getParameter("user_name"));
        user_info.setPwd(request.getParameter("pwd"));
        CookieUtil.writeCookie(response,"user_name", user_info.getUser_name());
        CookieUtil.writeCookie(response,"pwd", user_info.getPwd());
        //System.out.println(user_info.getPwd() + "|" + user_info.getUser_name());
        user_infoService.checkLogin(user_info);
        return "index";
    }*/

}
