package com.mall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mall.entity.*;
import com.mall.service.*;
import com.mall.util.GetTime;
import com.mall.util.RedisUtil;
import com.mall.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class COrder {
    @Autowired
    public OrderService orderService;
    @Autowired
    public Ratings_Service ratingsService;
    @Autowired
    public Address_infoService address_infoService;



    //进行商品的评价 ajax形式
    @RequestMapping("eva_ing")
    public String evaing(HttpServletRequest request, HttpServletResponse response, Model model){
        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        String user_name = request.getSession().getAttribute("user_name").toString();
        int skuid=Integer.parseInt(request.getParameter("skuid").toString());
        int itemid=Integer.parseInt(request.getParameter("itemid").toString());
        int orderid=Integer.parseInt(request.getParameter("orderid").toString());
        int ratings=Integer.parseInt(request.getParameter("evavalue").toString());
        //System.out.println(ratings);
        System.out.println(user_id);
        //更新order_item表，表示该商品已经评价过了
        orderService.itemupdate(itemid);

        Ratings_info ratingsinfo=new Ratings_info();
        ratingsinfo.setUser_id(user_id);
        ratingsinfo.setItem_id(skuid);
        ratingsinfo.setRating(ratings);
        ratingsinfo.setTimestamp(GetTime.getGuid());
        ratingsService.insertratings(ratingsinfo);

        return "ok";
    }
    //出去生成订单
    @RequestMapping("cancelcheckorder")
    public String cancelcheckorder(HttpServletRequest request, HttpServletResponse response, Model model) {
        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        String user_name = request.getSession().getAttribute("user_name").toString();
        String buyerCartValue = null;
        RedisUtil redisUtil = null;
        redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");
        buyerCartValue = redisUtil.get(user_name);
        BuyerCart buyerCart = null;
        buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
        if(buyerCart!=null){
            for(int i=0;i<buyerCart.getItems().size();i++){
                if(buyerCart.getItems().get(i).isChecked()){
                    buyerCart.getItems().get(i).setChecked(false);
                }
            }
        }
        String fromObject = JSON.toJSONString(buyerCart);
        redisUtil.set(user_name, fromObject.toString());
        return "ok";
    }

}
