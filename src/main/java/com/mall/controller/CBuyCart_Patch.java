package com.mall.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mall.entity.BuyerItem_Patch;
import com.mall.entity.Sku_info;
import com.mall.service.BuyerCart_Patch;
import com.mall.service.Sku_infoService;
import com.mall.util.*;
import com.alibaba.fastjson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CBuyCart_Patch {
    @Autowired private Sku_infoService sku_infoService;
    @RequestMapping("add_cart_patch")
    public String add_Cart(HttpServletRequest request,
                           HttpServletResponse response, Model model) {
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        String user_select_property = request.getParameter
                ("user_select_property").toString();
        //转换生成成Sku_info类型对象。
        Sku_info sku_info = sku_infoService.getSingleSku_info(sku_id);
        sku_info.setUser_select_property(user_select_property);
        sku_info.setBuyAmount(amount);
        //以后在此位置添加商品的属性。
        BuyerCart_Patch buyerCart = null;
        BuyerItem_Patch buyerItem = new BuyerItem_Patch();
        buyerItem.addSku_info(sku_info);
        String user_name = null;
        if (request.getSession().getAttribute("user_name") != null)
            user_name = request.getSession().getAttribute("user_name").toString();
        String buyerCartValue = null;
        RedisUtil redisUtil = null;
        if (user_name == null){
            buyerCartValue = CookieUtil.getCookie(request,
                    "buyerCart");
        }
        else{redisUtil = (RedisUtil) SpringUtil.applicationContext.
                    getBean("redisUtil");
            buyerCartValue = redisUtil.get(user_name);
        }
        if(buyerCartValue == null){//需要测试一下哪个不出错。
            //如果等于null，说明没有购物车信息存在，需要重新构造一个购物车。
            buyerCart = new BuyerCart_Patch();
        }
        else{//说明存在购物车字符串键值，取出来反序列化成购物车对象。
            buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart_Patch>(){});
        }
        boolean flag1 = false;
        boolean flag2 = false;
        for (int i = 0; i < buyerCart.getItems().size(); i++){
           //先找到卖家。
            BuyerItem_Patch item = buyerCart.getItems().get(i);
            if (sku_info.getSeller_id() == item.getSeller_id()){
                //去查找商品。找到就修改数量。找不到就添加商品
                for (int j = 0; j < item.getSku_infoList().size(); j++){
                    if (sku_info.getSku_id() ==
                    item.getSku_infoList().get(j).getSku_id()) {
                        item.getSku_infoList().get(j).incrementAmount(amount);
                        flag1 = true;
                        break;
                    }
                }//卖家存在，但是商品不存在。添加商品
                if (!flag1)
                    item.addSku_info(sku_info);
                flag2 = true;
                break;
            }
        }//卖家都不存在。则添加卖家和卖家商品
        if (!flag2){
            BuyerItem_Patch addItem = new BuyerItem_Patch();
            addItem.setSeller_id(sku_info.getSeller_id());
            addItem.setSeller_name(sku_info.getSeller_name());
            addItem.addSku_info(sku_info);
            buyerCart.addItem(addItem);
        }
        //序列化成字符串。
        String fromObject = JSON.toJSONString(buyerCart);
        if(user_name == null)//根据用户是否登录把购物车存入到cookie或者redis。
            CookieUtil.writeCookie(response, "buyerCart", fromObject);
        else
            redisUtil.set(user_name, fromObject.toString());

        return "shopcart_patch";
    }

    @RequestMapping("show_cart_patch")
    public String show_Cart(HttpServletRequest request,
                           HttpServletResponse response, Model model) {
        BuyerCart_Patch buyerCart = null;
        String user_name = null;
        if (request.getSession().getAttribute("user_name") != null)
            user_name = request.getSession().getAttribute("user_name").toString();
        String buyerCartValue = null;
        RedisUtil redisUtil = null;
        if (user_name == null){
            buyerCartValue = CookieUtil.getCookie(request,
                    "buyerCart");
        }
        else{redisUtil = (RedisUtil) SpringUtil.applicationContext.
                getBean("redisUtil");
            buyerCartValue = redisUtil.get(user_name);
        }
        if(buyerCartValue == null){//需要测试一下哪个不出错。
            //如果等于null，说明没有购物车信息存在，需要重新构造一个购物车。
            buyerCart = new BuyerCart_Patch();
        }
        else{//说明存在购物车字符串键值，取出来反序列化成购物车对象。
            buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart_Patch>(){});
        }
        model.addAttribute("buyerCart", buyerCart);
        return "shopcart_patch";
    }
}