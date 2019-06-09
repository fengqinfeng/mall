package com.mall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mall.entity.Class_info;
import com.mall.entity.User_info;
import com.mall.service.BuyerCart;
import com.mall.service.Class_infoService;
import com.mall.service.User_infoService;
import com.mall.util.CookieUtil;
import com.mall.util.RedisUtil;
import com.mall.util.SpringUtil;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CUser_Info_Thymeleaf {
    @Autowired
    private User_infoService user_infoService;
    @Autowired
    private Class_infoService classinfoservice;
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
        List<Class_info>ans =new ArrayList<Class_info>();
        ans=classinfoservice.class_all();
        BuyerCart buyerCart = new BuyerCart();
        String user_name = null;
        String buyerCartValue = null;
        RedisUtil redisUtil = null;
        if (request.getSession().getAttribute("user_name") != null)
            user_name = request.getSession().getAttribute("user_name").toString();
        if (user_name == null){//说明用户没有登录。去cookie里面找购物车信息json字符串信息。
            //利用buyerCart字符串作为键名，写死了。可以自己灵活修改。
            // 购物车的值作为键值。
            buyerCartValue = CookieUtil.getCookie(request, "buyerCart");
            // System.out.println(buyerCartValue);
        }
        else{//用户登录了。则去数据库或者redis里面进行获取购物车信息。
            //利用用户名作为键名，购物车的值作为键值。
            redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");//从spring容器里面得到一个对象
            buyerCartValue = redisUtil.get(user_name);
        }
        if(buyerCartValue!=null){//说明存在购物车字符串键值，取出来反序列化成购物车对象。
            buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
        }
        int sum=0;
        for(int i=0;i<buyerCart.getItems().size();i++){
            if(buyerCart.getItems().get(i).isHave()==true){
                sum++;
            }
        }
        if(buyerCartValue==null)sum=0;
        model.addAttribute("sum",sum);
        model.addAttribute("ans",ans);
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
