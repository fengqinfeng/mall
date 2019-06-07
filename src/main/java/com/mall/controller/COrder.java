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
    @Autowired
    public Sku_infoService skuinfoservice;
    //删除订单
    @RequestMapping("deleorder")
    public String deleorder(HttpServletRequest request, HttpServletResponse response, Model model){
        int id=Integer.parseInt(request.getParameter("sku_id").toString());
        orderService.orderdele(id);
        return "ok";
    }


    //取消订单
    @RequestMapping("cancelorder")
    public String cancelorder(HttpServletRequest request, HttpServletResponse response, Model model){
        int id=Integer.parseInt(request.getParameter("sku_id").toString());
        orderService.ordercancel(id);
        return "ok";
    }

    //立即支付
    @RequestMapping("paynow")
     public String payn(HttpServletRequest request, HttpServletResponse response, Model model){
        int id=Integer.parseInt(request.getParameter("id").toString());
        int maxid=orderService.maxid();

        for(int i=id+1;i<=maxid;i++){
            orderService.payed(i,"1");

        }
        return "ok";

    }
    //支付单个订单
    @RequestMapping("payo")
    public String payo(HttpServletRequest request, HttpServletResponse response, Model model){
        int id=Integer.parseInt(request.getParameter("id").toString());

        List<Order_item_info>orderid=orderService.seleorderid(id);
        //购买时 要检查库存问题，购买成功是减库存，加销量
        //下标是商品id,值是购买数量
        int kucun[]=new int[100];
        for(int i=0;i<orderid.size();i++){
            kucun[orderid.get(i).getSku_id()]+=orderid.get(i).getAmount();
        }
        for(int i=0;i<100;i++){
            if(kucun[i]!=0){
                Sku_info ans=skuinfoservice.seleall(i);
                if(ans.getAmount()>=kucun[i]){
                    ans.setAmount(ans.getAmount()-kucun[i]);
                    ans.setSale_number(ans.getSale_number()+kucun[i]);
                    skuinfoservice.updateku(ans);
                }else{
                    return "err";
                }
            }
        }
        orderService.payed(id,"1");

        return "ok";
    }


    //确认订单后之后就生成订单
    @RequestMapping("paymoney")
    public String paymoney(HttpServletRequest request, HttpServletResponse response, Model model){
        //根据用户的id或者姓名，取得默认地址。显示在界面上。
        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        String user_name = request.getSession().getAttribute("user_name").toString();
        String add=request.getParameter("address").toString();

        //获取默认地址。没有默认地址的情况先不管
        Address_info address_info = address_infoService.selectoneaddress(user_id,add);
        //从redis购物车里面取出来选中的商品，显示在界面上
        RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");//从spring容器里面得到一个对象
        String buyerCartValue = redisUtil.get(user_name);
        BuyerCart buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>() {});
        String dd=redisUtil.get(user_name + "_check_item");
        //如果这个用户没有订单信息
        //System.out.println(dd);
        if(dd==null){
            //System.out.println("y");
            BuyerCart_Patch buyercartpatch=new BuyerCart_Patch();
            String fromObject = JSON.toJSONString(buyercartpatch);
            redisUtil.set(user_name + "_check_item", fromObject.toString());
            dd=redisUtil.get(user_name + "_check_item");
        }
        BuyerCart_Patch ans=JSON.parseObject(dd, new TypeReference<BuyerCart_Patch>() {});
        //因为一起结算，最后可能生成不止一个订单
        List<BuyerItem_Patch> item_patchList = new ArrayList<BuyerItem_Patch>();
        //System.out.println(buyerCart.getItems().get(0).getSku_info().getSeller_name());

        //isCheckedItem.setSeller_id(-1);
        List<Sku_info> te=new ArrayList<>();
        int pan[]=new int[100];
        for (int i = 0; i < buyerCart.getItems().size(); i++) {
            //表示有这个卖家id
            if(buyerCart.getItems().get(i).isChecked()==true){
                pan[buyerCart.getItems().get(i).getSku_info().getSeller_id()]=1;
                //System.out.println(buyerCart.getItems().get(i).getSku_info().getSku_id());
            }

        }
        for(int i=0;i<pan.length;i++){
            if(pan[i]==1){
                //System.out.println(i);
                BuyerItem_Patch isCheckedItem = new BuyerItem_Patch();
                for(int j=0;j<buyerCart.getItems().size();j++){
                    int num=0;
                    //System.out.println(buyerCart.getItems().get(j).getSku_info().getSeller_id());
                    if(buyerCart.getItems().get(j).isChecked()&&buyerCart.getItems().get(j).getSku_info().getSeller_id()==i){
                        //System.out.println(1);
                        isCheckedItem.setSeller_id(buyerCart.getItems().get(j).getSku_info().getSeller_id());
                        isCheckedItem.setSeller_name(buyerCart.getItems().get(j).getSku_info().getSeller_name());
                        buyerCart.getItems().get(j).setChecked(false);
                        buyerCart.getItems().get(j).setHave(false);

                        Sku_info inn=buyerCart.getItems().get(j).getSku_info();
                        inn.setBuyAmount(buyerCart.getItems().get(j).getAmount());
                        isCheckedItem.addSku_info(inn);
                        //System.out.println(isCheckedItem.getSku_infoList().get(0).getProduct_class_name());
                        //System.out.println(buyerCart.getItems().get(j).getSku_info().getProduct_class_name());
                    }
                }
                item_patchList.add(isCheckedItem);
            }
        }
        //itemlist是一个用户的所有订单列表，其中一个订单包含多个商品
        //isCheckedItem.setSku_infoList(te);
        //存在卖家信息
//        if (isCheckedItem.getSeller_id() != -1)
//            item_patchList.add(isCheckedItem);  //加入新的订单信息
        if (item_patchList!=null){
            for(int i=0;i<item_patchList.size();i++) {
                ans.addItem(item_patchList.get(i));
            }
        }
        orderService.InsertOrder_Patch(item_patchList,user_id,address_info);

        String fromObject = JSON.toJSONString(ans);
        redisUtil.set(user_name + "_check_item", fromObject.toString());

        String fromObject2 = JSON.toJSONString( buyerCart);
        redisUtil.set(user_name, fromObject2.toString());
        return "ok";
    }

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
