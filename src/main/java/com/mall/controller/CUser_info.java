package com.mall.controller;
import com.mall.entity.User_info;
import com.mall.service.User_infoService;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class CUser_info {
    @Autowired
    private User_infoService user_infoService;
    @RequestMapping("/testString")
    public String testString(){
        return "testString......";
    }
    @RequestMapping("/AllAddressOfUser")
    public User_info selAllAddressOfUser(HttpServletRequest request){
        return user_infoService.selAllAddressOfUser(1);
    }
    /*
    @RequestMapping("checkLogin")
    public String checkLogin(HttpServletRequest request){
        User_info user_info = new User_info();
        user_info.setPwd(request.getParameter("pwd").toString());
        user_info.setUser_name(request.getParameter("user_name").toString());
        int i = user_infoService.checkLogin(user_info);
        if (i > 0) {//设置用户信息到session里面。
            request.getSession().setAttribute("user_name", user_info.getUser_name());
            return "success";
        }
        else
            return "error";
     }*/
/*
    @RequestMapping(value = "checkLoginMultiParam", method = RequestMethod.POST)
    public String checkLoginMultiParam(HttpServletRequest request){
        User_info user_info = new User_info();
        String user_name = request.getParameter("user_name").toString();
        String pwd = request.getParameter("pwd").toString();
        user_info.setPwd(pwd);
        user_info.setUser_name(user_name);
        if (user_infoService.checkLogin(user_info) > 0)
            return "success";
        else
            return "error";
    }*/
    @RequestMapping("selAll")
    public List<User_info> selAll(){
        return user_infoService.selAll();
    }
    @RequestMapping("selUser_info")
    public User_info selUser_info(){
        return user_infoService.selUser_info(2);
    }
}
