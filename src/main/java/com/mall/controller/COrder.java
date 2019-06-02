package com.mall.controller;

import com.mall.entity.Order_item_info;
import com.mall.entity.Ratings_info;
import com.mall.service.OrderService;
import com.mall.service.Ratings_Service;
import com.mall.util.GetTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class COrder {
    @Autowired
    public OrderService orderService;
    @Autowired
    public Ratings_Service ratingsService;
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

}
