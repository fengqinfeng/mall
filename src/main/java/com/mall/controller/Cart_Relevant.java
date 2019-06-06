package com.mall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mall.entity.BuyerItem;
import com.mall.entity.Sku_info;
import com.mall.service.BuyerCart;
import com.mall.service.Sku_infoService;
import com.mall.util.CookieUtil;
import com.mall.util.RedisUtil;
import com.mall.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class Cart_Relevant {
    @Autowired
    private Sku_infoService sku_infoService;


    @ResponseBody
    @RequestMapping("modify_buyerItem_selectall")
    public String modifybuyerItemselectall(HttpServletRequest request,HttpServletResponse response){
        String user_name = request.getParameter("user_name");
        if( request.getSession().getAttribute("user_name")!=null){
            RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.
                    getBean("redisUtil");
            String buyerCartValue = redisUtil.get(user_name);
            BuyerCart buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
            for (int i = 0; i < buyerCart.getItems().size(); i++){
                if (buyerCart.getItems().get(i).isHave()==true&&buyerCart.getItems().get(i).isChecked()==false){
                    buyerCart.getItems().get(i).setChecked(true);
                    //System.out.println(buyerCart.getItems().get(i).isChecked());

                }
            }
            String fromObject = JSON.toJSONString(buyerCart);
            redisUtil.set(user_name, fromObject.toString());
        }

        return "ok";
    }


    @ResponseBody
    @RequestMapping("modify_buyerItem_selectall_cancel")
    public String modifybuyerItemselectallcancel(HttpServletRequest request,HttpServletResponse response){
        String user_name = request.getParameter("user_name");
        if( request.getSession().getAttribute("user_name")!=null){
            RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.
                    getBean("redisUtil");
            String buyerCartValue = redisUtil.get(user_name);
            BuyerCart buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
            for (int i = 0; i < buyerCart.getItems().size(); i++){
                if (buyerCart.getItems().get(i).isHave()==true&&buyerCart.getItems().get(i).isChecked()==true){
                    buyerCart.getItems().get(i).setChecked(false);
                    //System.out.println(buyerCart.getItems().get(i).isChecked());

                }
            }
            String fromObject = JSON.toJSONString(buyerCart);
            redisUtil.set(user_name, fromObject.toString());
        }

        return "ok";
    }

    @ResponseBody
    @RequestMapping("modify_buyerItem_check")
    public int modifyBuyerItemCheck(HttpServletRequest request,HttpServletResponse response){
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        String property=request.getParameter("spproperty");
        String user_name = request.getParameter("user_name");
        if( request.getSession().getAttribute("user_name")!=null){
            RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.
                    getBean("redisUtil");
            String buyerCartValue = redisUtil.get(user_name);
            BuyerCart buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
            for (int i = 0; i < buyerCart.getItems().size(); i++){
                if (buyerCart.getItems().get(i).getSku_info().getSku_id() == sku_id&&
                        buyerCart.getItems().get(i).getSku_info().getUser_select_property().equals(property)){
                    buyerCart.getItems().get(i).setChecked(true);
                    System.out.println(buyerCart.getItems().get(i).isChecked());
                    break;
                }
            }
            String fromObject = JSON.toJSONString(buyerCart);
            redisUtil.set(user_name, fromObject.toString());
        }
        return 1;
    }

    //取消选择商品
    @ResponseBody
    @RequestMapping("modify_buyerItem_uncheck")
    public int modifyBuyerItemUncheck(HttpServletRequest request,HttpServletResponse response){
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        String property=request.getParameter("spproperty");
        String user_name = request.getParameter("user_name");
        if( request.getSession().getAttribute("user_name")!=null){
            RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.
                    getBean("redisUtil");
            String buyerCartValue = redisUtil.get(user_name);
            BuyerCart buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
            for (int i = 0; i < buyerCart.getItems().size(); i++){
                if (buyerCart.getItems().get(i).getSku_info().getSku_id() == sku_id&&
                        buyerCart.getItems().get(i).getSku_info().getUser_select_property().equals(property)){
                    buyerCart.getItems().get(i).setChecked(false);
                    System.out.println(buyerCart.getItems().get(i).isChecked());
                    break;
                }
            }
            String fromObject = JSON.toJSONString(buyerCart);
            redisUtil.set(user_name, fromObject.toString());
        }
        return 1;
    }
    //通过ajax技术修改商品的数量。
    @ResponseBody
    @RequestMapping("modify_buyerItem_amount_dec")
    public int modifyBuyerItemAmountDec(HttpServletRequest request,HttpServletResponse response){
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        String property=request.getParameter("spproperty");
        String user_name = request.getParameter("user_name");
       // System.out.println(request.getSession().getAttribute("user_name"));
        if( request.getSession().getAttribute("user_name")!=null){
            RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.
                    getBean("redisUtil");
            String buyerCartValue = redisUtil.get(user_name);
            BuyerCart buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
            for (int i = 0; i < buyerCart.getItems().size(); i++){
                if (buyerCart.getItems().get(i).getSku_info().getSku_id() == sku_id&&
                        buyerCart.getItems().get(i).getSku_info().getUser_select_property().equals(property)){
                    buyerCart.getItems().get(i).setAmount(amount-1);
                    break;
                }
            }
            String fromObject = JSON.toJSONString(buyerCart);
            redisUtil.set(user_name, fromObject.toString());
        }
        else{
            String temp= CookieUtil.getCookie(request, "buyerCart");
            BuyerCart buyerCart = JSON.parseObject(temp, new TypeReference<BuyerCart>(){});
            for (int i = 0; i < buyerCart.getItems().size(); i++){
                if (buyerCart.getItems().get(i).getSku_info().getSku_id() == sku_id&&
                        buyerCart.getItems().get(i).getSku_info().getUser_select_property().equals(property)){
                    buyerCart.getItems().get(i).setAmount(amount-1);
                    break;
                }
            }
            String ans = JSON.toJSONString(buyerCart);
            CookieUtil.writeCookie( response,"buyerCart",ans);
            //System.out.println(temp);
        }

        return  amount-1;
    }

    @ResponseBody
    @RequestMapping("modify_buyerItem_amount_up")
    public int modifyBuyerItemAmountUp(HttpServletRequest request,HttpServletResponse response){
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        String property=request.getParameter("spproperty");
        String user_name = request.getParameter("user_name");
      //  System.out.println(request.getSession().getAttribute("user_name"));
        if( request.getSession().getAttribute("user_name")!=null){
            RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");
            String buyerCartValue = redisUtil.get(user_name);
            BuyerCart buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
            for (int i = 0; i < buyerCart.getItems().size(); i++){
                if (buyerCart.getItems().get(i).getSku_info().getSku_id() == sku_id&&
                        buyerCart.getItems().get(i).getSku_info().getUser_select_property().equals(property)){
                    buyerCart.getItems().get(i).setAmount(amount+1);
                    break;
                }
            }
            String fromObject = JSON.toJSONString(buyerCart);
            redisUtil.set(user_name, fromObject.toString());
        }
        else{
            String temp= CookieUtil.getCookie(request, "buyerCart");
            BuyerCart buyerCart = JSON.parseObject(temp, new TypeReference<BuyerCart>(){});
            for (int i = 0; i < buyerCart.getItems().size(); i++){
                if (buyerCart.getItems().get(i).getSku_info().getSku_id() == sku_id&&
                        buyerCart.getItems().get(i).getSku_info().getUser_select_property().equals(property)){
                    buyerCart.getItems().get(i).setAmount(amount+1);
                    break;
                }
            }
            String ans = JSON.toJSONString(buyerCart);
            CookieUtil.writeCookie( response,"buyerCart",ans);
            //System.out.println(temp);
        }

        return  amount+1;
    }

    //立即购买时先加入购物车
    @RequestMapping("lijiaddcart")
    @ResponseBody
    public String lijibuyaddcart(HttpServletRequest request, HttpServletResponse response, Model model){
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        String user_select_property = request.getParameter("user_select_property").toString();
        Sku_info sku_info = sku_infoService.getSingleSku_info(sku_id);
        sku_info.setUser_select_property(user_select_property);
        //sku_info.setChecked(true);
        //以后在此位置添加商品的属性。
        BuyerCart buyerCart = null;
        BuyerItem buyerItem = new BuyerItem();
        buyerItem.setSku_info(sku_info);
        buyerItem.setAmount(amount);
        buyerItem.setChecked(true);
        String user_name = null;
        if (request.getSession().getAttribute("user_name") != null)
            user_name = request.getSession().getAttribute("user_name").toString();
        String buyerCartValue = null;
        RedisUtil redisUtil = null;
        redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");
        buyerCartValue = redisUtil.get(user_name);
        if(buyerCartValue == null) {
            //如果等于null，说明没有购物车信息存在，需要重新构造一个购物车。
            buyerCart = new BuyerCart();
            buyerCart.addItem(buyerItem);
        }else{
            buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
            int index = -1;
            if(buyerCart.getItems() != null || buyerCart.getItems().size() > 0)
                index = buyerCart.findItem(buyerItem);
            if(index>=0){
                //if( buyerCart.getItems().get(index).isHave()==false){
                    buyerCart.getItems().get(index).setHave(true);
                    buyerCart.getItems().get(index).setChecked(true);
                    buyerCart.getItems().get(index).setAmount(amount);
               // }else{
                    //buyerCart.getItems().get(index).incrementAmount(amount);
                    //buyerCart.getItems().get(index).setChecked(true);
               // }
            }else
            {
                buyerCart.addItem(buyerItem);
            }
        }
        String fromObject = JSON.toJSONString(buyerCart);
        redisUtil.set(user_name, fromObject.toString());
        return "ok";
    }

    @RequestMapping("addcart")
    @ResponseBody
    public String test(HttpServletRequest request, HttpServletResponse response, Model model){
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        String user_select_property = request.getParameter("user_select_property").toString();
        //转换生成成Sku_info类型对象。
        Sku_info sku_info = sku_infoService.getSingleSku_info(sku_id);
        sku_info.setUser_select_property(user_select_property);
        //sku_info.setChecked(true);
        //以后在此位置添加商品的属性。
        BuyerCart buyerCart = null;
        BuyerItem buyerItem = new BuyerItem();
        buyerItem.setSku_info(sku_info);
        buyerItem.setAmount(amount);
        //buyerItem.getSku_info().setUser_select_property(user_select_property);
        //buyerItem.getSku_info().setChecked(true);
        String user_name = null;
        if (request.getSession().getAttribute("user_name") != null)
            user_name = request.getSession().getAttribute("user_name").toString();


        String buyerCartValue = null;
        RedisUtil redisUtil = null;
        if (user_name == null){
            //说明用户没有登录。去cookie里面找购物车信息json字符串信息
            //利用buyerCart字符串作为键名
            // 购物车的值作为键值
            buyerCartValue = CookieUtil.getCookie(request, "buyerCart");
            if(buyerCartValue == null){
                //如果等于null，说明没有购物车信息存在，需要重新构造一个购物车。
                buyerCart = new BuyerCart();
                buyerCart.addItem(buyerItem);
                String fromObject = JSON.toJSONString(buyerCart);
                CookieUtil.writeCookie(response, "buyerCart", fromObject);
            }else{
                buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
                int index = -1;
                if(buyerCart.getItems() != null || buyerCart.getItems().size() > 0)
                    index = buyerCart.findItem(buyerItem);
                if(index>=0){
                    //被删掉或者导入过redis
                    if( buyerCart.getItems().get(index).isHave()==false||buyerCart.getItems().get(index).isImpo()==true){
                        buyerCart.getItems().get(index).setHave(true);
                        buyerCart.getItems().get(index).setChecked(false);//不选中
                        buyerCart.getItems().get(index).setImpo(false);//没导入
                        buyerCart.getItems().get(index).setAmount(amount);
                    }else{
                        buyerCart.getItems().get(index).incrementAmount(amount);
                    }
                }else
                {
                    buyerCart.addItem(buyerItem);
                }
                String fromObject = JSON.toJSONString(buyerCart);
                CookieUtil.writeCookie(response, "buyerCart", fromObject);
            }
        }else
        {
            redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");
            buyerCartValue = redisUtil.get(user_name);
            if(buyerCartValue == null) {
                //如果等于null，说明没有购物车信息存在，需要重新构造一个购物车。
                buyerCart = new BuyerCart();
                buyerCart.addItem(buyerItem);
            }else{
                buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
                int index = -1;
                if(buyerCart.getItems() != null || buyerCart.getItems().size() > 0)
                    index = buyerCart.findItem(buyerItem);
                if(index>=0){
                    if( buyerCart.getItems().get(index).isHave()==false){
                        buyerCart.getItems().get(index).setHave(true);
                        buyerCart.getItems().get(index).setAmount(amount);
                    }else{
                        buyerCart.getItems().get(index).incrementAmount(amount);
                    }
                }else
                {
                    buyerCart.addItem(buyerItem);
                }
            }
            String fromObject = JSON.toJSONString(buyerCart);
            redisUtil.set(user_name, fromObject.toString());

        }
        //存入成功后刷新购物车或者购物车的js代码。
        //System.out.println("添加到redis或者cookie里面的内容为：" + fromObject.toString());
        //return "redirect:show_cart";
        return "ok";
    }

    @RequestMapping("deletecart")
    @ResponseBody
    public String deleteCart(HttpServletRequest request, HttpServletResponse response, Model model){
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        String property=request.getParameter("spproperty");
        String user_name = request.getParameter("user_name");
        if( request.getSession().getAttribute("user_name")!=null) {
            RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");
            String buyerCartValue = redisUtil.get(user_name);
            BuyerCart buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
            for (int i = 0; i < buyerCart.getItems().size(); i++){
                if (buyerCart.getItems().get(i).getSku_info().getSku_id() == sku_id&&
                        buyerCart.getItems().get(i).getSku_info().getUser_select_property().equals(property)){
                    buyerCart.getItems().get(i).setHave(false);
                    break;
                }
            }
            String fromObject = JSON.toJSONString(buyerCart);
            redisUtil.set(user_name, fromObject.toString());
        }else{
            String temp= CookieUtil.getCookie(request, "buyerCart");
            BuyerCart buyerCart = JSON.parseObject(temp, new TypeReference<BuyerCart>(){});
            for (int i = 0; i < buyerCart.getItems().size(); i++){
                if (buyerCart.getItems().get(i).getSku_info().getSku_id() == sku_id&&
                        buyerCart.getItems().get(i).getSku_info().getUser_select_property().equals(property)){
                    buyerCart.getItems().get(i).setHave(false);
                    break;
                }
            }
            String ans = JSON.toJSONString(buyerCart);
            CookieUtil.writeCookie( response,"buyerCart",ans);
        }
        return "ok";
    }
}
