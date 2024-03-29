package com.mall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.entity.*;
import com.mall.service.*;
import com.mall.util.RedisUtil;
import com.mall.util.SpringUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class COrder_Thymeleaf {
    @Autowired OrderService orderService;
    @Autowired Address_infoService address_infoService;
    @Autowired Sku_infoService sku_infoService;


    @RequestMapping("tee")
    public String tee(){
        return "redirect:show_order2";
    }

    @RequestMapping("check_order")
    public String checkorder(){
        return "check_order";
    }


    //跳到单个支付的页面
    @RequestMapping("paysingle")
    public String paysingle(HttpServletRequest request, HttpServletResponse response, Model model){
        int money=Integer.parseInt(request.getParameter("money").toString());
        int id=Integer.parseInt(request.getParameter("id").toString());
        model.addAttribute("sum",money);
        model.addAttribute("orderid",id);

        return "payone";
    }


    //跳转到评价页面
    @RequestMapping("evalu_page")
    public String eva(HttpServletRequest request, Model model){
        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        String user_name = request.getSession().getAttribute("user_name").toString();
        int itemid=Integer.parseInt(request.getParameter("itemid").toString());
        int skuid=Integer.parseInt(request.getParameter("skuid").toString());
        Sku_info sku=sku_infoService.getSingleSku_info(skuid);
        Order_item_info orderitem=orderService.orderiteminfo(itemid);
        model.addAttribute("sku",sku);
        model.addAttribute("orderitem",orderitem);

        return "commend";
    }



    //显示付款页面
    @RequestMapping("paym")
    public String paym(HttpServletRequest request, HttpServletResponse response, Model model){
        String sum=request.getParameter("sum").toString();
        int orderid=Integer.parseInt(request.getParameter("orderid").toString());
        model.addAttribute("orderid",orderid);
        model.addAttribute("sum",sum);
        return "pay";
    }




    //显示所有订单
    @RequestMapping("show_order2")
    public String showOrder2(HttpServletRequest request, Model model, @RequestParam(required = false,defaultValue = "1",value = "pn")Integer pn,
                             Map<String,Object> map){
        //根据用户的id或者姓名，取得默认地址。显示在界面上。

        int user_id=0;
        String user_name;
        if(request.getSession().getAttribute("user_id")!=null){
            user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        }
        if(request.getSession().getAttribute("user_name")!=null){
            user_name = request.getSession().getAttribute("user_name").toString();
        }
        if(user_id==0){
            return "login";
        }

        PageHelper.startPage(pn,10);
        List<Order_info> ans=orderService.showorderbysql(user_id);
        PageInfo pageInfo = new PageInfo<>(ans,5);
        System.out.println(pageInfo);
        model.addAttribute("ans", ans);
        map.put("pageInfo",pageInfo);
        return "order2";
    }
    //显示已付款的订单
    @RequestMapping("show_order2_payed")
    public String showorder2payed(HttpServletRequest request, Model model, @RequestParam(required = false,defaultValue = "1",value = "pn")Integer pn,
                                  Map<String,Object> map){
        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        String user_name = request.getSession().getAttribute("user_name").toString();
        PageHelper.startPage(pn,10);
        List<Order_info> ans=orderService.orderinfo_payed(user_id);
        PageInfo pageInfo = new PageInfo<>(ans,5);
        System.out.println(pageInfo);
        model.addAttribute("ans", ans);
        map.put("pageInfo",pageInfo);
        return "order2";
    }

    //已作废
    @RequestMapping("show_order")
    public String showOrder(HttpServletRequest request, Model model){
        //根据用户的id或者姓名，取得默认地址。显示在界面上。
        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        String user_name = request.getSession().getAttribute("user_name").toString();
        //获取默认地址。没有默认地址的情况先不管
        Address_info address_info = address_infoService.selDefaultAddress(user_id);
        //从redis购物车里面取出来选中的商品，显示在界面上
        RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");//从spring容器里面得到一个对象
        String dd=redisUtil.get(user_name + "_check_item");
        //如果这个用户没有订单信息
        //System.out.println(dd);
        if(dd==null){

        }
        BuyerCart_Patch ans=JSON.parseObject(dd, new TypeReference<BuyerCart_Patch>() {});
        model.addAttribute("ans", ans);
        return "order2";
    }


    //确认订单页面
    @RequestMapping("checkorder")
    public String checko(HttpServletRequest request, Model model) {
        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        String user_name = request.getSession().getAttribute("user_name").toString();
        List<Address_info> address=new ArrayList<Address_info>();
        address=address_infoService.selAllAddress(user_id);
        System.out.print(address);
        //从redis购物车里面取出来选中的商品，显示在界面上
        RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");//从spring容器里面得到一个对象
        //取到该用户在redis购物车里的商品
       String buyerCartValue = redisUtil.get(user_name);
        BuyerCart buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>() {});
        //重新开订单信息
        BuyerCart_Patch buyercartpatch=new BuyerCart_Patch();

        //因为一起结算，最后可能生成不止一个订单
        List<BuyerItem_Patch> item_patchList = new ArrayList<BuyerItem_Patch>();
        List<Sku_info> te=new ArrayList<>();
        int pan[]=new int[100];
        for (int i = 0; i < buyerCart.getItems().size(); i++) {
            //表示有这个卖家id
            if(buyerCart.getItems().get(i).isChecked()==true){
                pan[buyerCart.getItems().get(i).getSku_info().getSeller_id()]=1;
            }

        }
        for(int i=0;i<pan.length;i++){
            if(pan[i]==1){
                System.out.println(i);
                BuyerItem_Patch isCheckedItem = new BuyerItem_Patch();
                for(int j=0;j<buyerCart.getItems().size();j++){
                    int num=0;

                    if(buyerCart.getItems().get(j).isChecked()&&buyerCart.getItems().get(j).getSku_info().getSeller_id()==i&&buyerCart.getItems().get(j).isHave()){
                        //System.out.println(1);
                        isCheckedItem.setSeller_id(buyerCart.getItems().get(j).getSku_info().getSeller_id());
                        isCheckedItem.setSeller_name(buyerCart.getItems().get(j).getSku_info().getSeller_name());

                        Sku_info inn=buyerCart.getItems().get(j).getSku_info();
                        inn.setBuyAmount(buyerCart.getItems().get(j).getAmount());
                        isCheckedItem.addSku_info(inn);
                    }
                }
                item_patchList.add(isCheckedItem);
            }
        }
        BuyerCart_Patch ans=new BuyerCart_Patch();
        ans.setItems(new ArrayList<BuyerItem_Patch>());
        if (item_patchList!=null){
            for(int i=0;i<item_patchList.size();i++) {
                ans.addItem(item_patchList.get(i));
            }
        }
        int orderid=orderService.maxid();
        model.addAttribute("orderid",orderid);
        model.addAttribute("ans",ans);
        model.addAttribute("address",address);
        return "check_order";
    }
    //生成订单
    @RequestMapping("add_order")
    public String confirm_Order(HttpServletRequest request, Model model) {
        //根据用户的id或者姓名，取得默认地址。显示在界面上。
        int user_id = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        String user_name = request.getSession().getAttribute("user_name").toString();

        //获取默认地址。没有默认地址的情况先不管
        Address_info address_info = address_infoService.selDefaultAddress(user_id);
        //从redis购物车里面取出来选中的商品，显示在界面上
        RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");//从spring容器里面得到一个对象
        String buyerCartValue = redisUtil.get(user_name);
        BuyerCart buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>() {});
        String dd=redisUtil.get(user_name + "_check_item");
        //如果这个用户没有订单信息
        if(dd==null){
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

        //BuyerCart_Patch checkedBuyerCart = new BuyerCart_Patch();
        //checkedBuyerCart.addItem();

        String fromObject = JSON.toJSONString(ans);
        redisUtil.set(user_name + "_check_item", fromObject.toString());

        String fromObject2 = JSON.toJSONString( buyerCart);
        redisUtil.set(user_name, fromObject2.toString());

        model.addAttribute("default_address", address_info);
        model.addAttribute("itemList", item_patchList);
        List<Order_info> order=orderService.showorderbysql(user_id);
        model.addAttribute("ans", order);
        //model.addAttribute("cart", checkedBuyerCart);
        return "order2";
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

}
