package com.mall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mall.entity.*;
import com.mall.service.*;
import com.mall.util.RedisUtil;
import com.mall.util.SpringUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class COrder_Thymeleaf {
    @Autowired OrderService orderService;
    @Autowired Address_infoService address_infoService;
    @Autowired Sku_infoService sku_infoService;
    @RequestMapping("confirm_order")
    public String confirm_Order(HttpServletRequest request, Model model) {
        //根据用户的id或者姓名，取得默认地址。显示在界面上。
        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        String user_name = request.getSession().getAttribute("user_name").toString();
        //获取默认地址。
        Address_info address_info = address_infoService.
                selDefaultAddress(user_id);
        //从redis购物车里面取出来选中的商品，显示在界面上
        RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");//从spring容器里面得到一个对象
        String buyerCartValue = redisUtil.get(user_name);
        BuyerCart_Patch buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart_Patch>() {});
        List<BuyerItem_Patch> item_patchList = new ArrayList<BuyerItem_Patch>();
        for (int i = 0; i < buyerCart.getItems().size(); i++) {
            //卖家信息
            BuyerItem_Patch item = buyerCart.getItems().get(i);
            BuyerItem_Patch isCheckedItem = new BuyerItem_Patch();
            isCheckedItem.setSeller_id(-1);
            for (int j = 0; j < item.getSku_infoList().size(); j++) {
                if (item.getSku_infoList().get(i).isChecked()) {
                    //说明被选中。
                    if (isCheckedItem.getSeller_id() == -1) {
                        isCheckedItem.setSeller_id(item.getSku_infoList().get(i).getSeller_id());
                        isCheckedItem.setSeller_name(item.getSku_infoList().get(i).getSeller_name());
                    }//选中的卖家信息和商品都加进来了。
                    isCheckedItem.addSku_info(item.getSku_infoList().get(i));
                }
            }
            //存在卖家信息
            if (isCheckedItem.getSeller_id() != -1)
                item_patchList.add(isCheckedItem);
        }
        BuyerCart_Patch checkedBuyerCart = new BuyerCart_Patch();
        checkedBuyerCart.setItems(item_patchList);
        String fromObject = JSON.toJSONString(checkedBuyerCart);
        redisUtil.set(user_name + "_check_item", fromObject.toString());

        model.addAttribute("default_address", address_info);
        model.addAttribute("itemList", item_patchList);
        return "shopcart_test";
    }

    @RequestMapping("buyerCart_order")
    public String buyerCart_Order(HttpServletRequest request, Model model){
        BuyerCart_Patch buyerCart = null;
        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        String user_name = request.getSession().getAttribute("user_name").toString();
        RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");
        String buyerCartValue = redisUtil.get(user_name + "_check_item");
        buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart_Patch>(){});
        Address_info address_info = new Address_info();
        address_info.setLinkman("唐红军");
        address_info.setAddress("杭州电子科技大学信息工程学院");
        address_info.setTelephone("18966165637");
        orderService.InsertOrder_Patch(buyerCart.getItems(), user_id, address_info);
        return "order";
    }
    @RequestMapping("sku_info_order") //直接购买走此流程
    public String sku_info_Order(HttpServletRequest request, Model model){
        //根据用户的id或者姓名，取得默认地址。显示在界面上。
        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        String user_name = request.getSession().getAttribute("user_name").toString();
        //获取默认地址。
        Address_info address_info = address_infoService.selDefaultAddress(user_id);
        List<BuyerItem> itemList = new ArrayList<BuyerItem>();
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        Sku_info sku_info = sku_infoService.getSingleSku_info(sku_id);
        BuyerItem buyerItem = new BuyerItem();
        buyerItem.setSku_info(sku_info);
        itemList.add(buyerItem);
        model.addAttribute("default_address", address_info);
        model.addAttribute("itemList", itemList);
        return "order";
    }
//    @RequestMapping("show_order")
//    public String show_Order(HttpServletRequest request, Model model){
//        //默认按时间查询。默认查询条件为空。按商品标题或者订单号查询。
//        //String queryCondition = request.getParameter("queryContent");
//
//        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
//        List<Order_info> order_infoList = orderService.showOrder(user_id, "");
//        model.addAttribute("order_infoList", order_infoList);
//        return "order";
//    }
}
