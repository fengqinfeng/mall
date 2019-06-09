package com.mall.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mall.entity.BuyerItem;
import com.mall.entity.Sku_info;
import com.mall.service.BuyerCart;
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

//@RestController
@Controller
public class CBuyCart {
    @Autowired
    private Sku_infoService sku_infoService;

    @RequestMapping("test_redis")
    public String testRedis(){
        RedisUtil redisUtil = (RedisUtil)SpringUtil.applicationContext.
                getBean("redisUtil");
        redisUtil.set("test_keyName", "value:1234567");
        return redisUtil.get("test_keyName");
    }
    //传入商品的主键ID和数量，根据主键通过Mybatis xml文件
    // 从数据库中取得商品信息，转换成Sku对象，存入buyerItem。
    // +数量生成一个明细：BuyerItem，把BuyerItem作为BuyerCart的属性
    // 放入购物车BuyerCart。做这件事之前，
    //要把购物车从redis或者cookie里面读出来，构造一个购物车。
    //第一步，尝试从cookie里面读出购物车信息的值。
    //先检测用户有没有登录
    //通过request或者thymeleaf自动绑定等方式获取商品的id，数量，
    // 和购买商品的尺寸颜色等属性，目前没有加入属性。
    @RequestMapping("add_cart")
    public String add_Cart(HttpServletRequest request,
                           HttpServletResponse response, Model model){
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        String user_select_property = request.getParameter("user_select_property").toString();
        //转换生成成Sku_info类型对象。
        Sku_info sku_info = sku_infoService.getSingleSku_info(sku_id);
        sku_info.setUser_select_property(user_select_property);
        //以后在此位置添加商品的属性。
        BuyerCart buyerCart = null;
        BuyerItem buyerItem = new BuyerItem();
        buyerItem.setSku_info(sku_info);
        buyerItem.setAmount(amount);
        String user_name = null;

        if (request.getSession().getAttribute("user_name") != null)
            user_name = request.getSession().getAttribute("user_name").toString();

        System.out.println(user_name);
        String buyerCartValue = null;
        RedisUtil redisUtil = null;

        if (user_name == null){//说明用户没有登录。去cookie里面找购物车信息json字符串信息。
            //利用buyerCart字符串作为键名，写死了。可以自己灵活修改。
            // 购物车的值作为键值。
           // System.out.println(12);
            buyerCartValue = CookieUtil.getCookie(request, "buyerCart");
            int temp=1;
            for (int i = 0; i < buyerCart.getItems().size(); i++){
                if (buyerCart.getItems().get(i).getSku_info().getSku_id()==sku_id){
                    System.out.println(buyerCart.getItems().get(i).isChecked());
                    if(buyerCart.getItems().get(i).isChecked()==false){
                        buyerCart.getItems().get(i).setAmount(amount);
                        buyerCart.getItems().get(i).setChecked(true);
                    }

                }

            }

        }
        else{//用户登录了。则去数据库或者redis里面进行获取购物车信息。
            //利用用户名作为键名，购物车的值作为键值。
            redisUtil = (RedisUtil) SpringUtil.applicationContext.
                    getBean("redisUtil");//从spring容器里面得到一个对象
            buyerCartValue = redisUtil.get(user_name);
        }
        //System.out.println("从cookie或者redis取出来的值:" + buyerCartValue);
        if(buyerCartValue == null){//需要测试一下哪个不出错。
            //如果等于null，说明没有购物车信息存在，需要重新构造一个购物车。
            buyerCart = new BuyerCart();
        }
        else{//说明存在购物车字符串键值，取出来反序列化成购物车对象。
            buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});
        }
        //添加购物车明细项到购物车
        //对购物车进行查找，是否存在该类商品，如果不存在，
        // 则进行直接添加，如果存在，则数量增加
        int index = -1;
        if(buyerCart.getItems() != null || buyerCart.getItems().size() > 0)
            index = buyerCart.findItem(buyerItem);
        if(index >= 0){
            //System.out.println("找到了index >= 0");
            buyerCart.getItems().get(index).incrementAmount(buyerItem.getAmount());
        }
        else
            buyerCart.addItem(buyerItem);
        //序列化成字符串。
        String fromObject = JSON.toJSONString(buyerCart);
        //System.out.println(fromObject);
        if(user_name == null)//根据用户是否登录把购物车存入到cookie或者redis。
            CookieUtil.writeCookie(response, "buyerCart", fromObject);
        else
            redisUtil.set(user_name, fromObject.toString());
        //存入成功后刷新购物车或者购物车的js代码。
        //System.out.println("添加到redis或者cookie里面的内容为：" + fromObject.toString());
        //return "redirect:show_cart";
        model.addAttribute("buyerCart", buyerCart);
        return  "RedirectView:shopcart";

    }
    @RequestMapping("delete_cart")
    public String delete_Cart(HttpServletRequest request){
        return "cart";
    }
    /* 分成2种情况，一种是没有登录，从cookie中读取，1种是登录了，
     * 从redis里面读取
     */
    @RequestMapping("show_cart")
    public String show_Cart(HttpServletRequest request, Model model){
        BuyerCart buyerCart = new BuyerCart();
        String user_name = null;
        String buyerCartValue = null;
        RedisUtil redisUtil = null;
        if (request.getSession().getAttribute("user_name") != null)
            user_name = request.getSession().getAttribute("user_name").toString();
        if (user_name == null){//说明用户没有登录。去cookie里面找购物车信息json字符串信息。
            //利用buyerCart字符串作为键名，写死了。可以自己灵活修改。
            // 购物车的值作为键值。
            //if(CookieUtil.getCookie(request, "buyerCart")!=null){
                buyerCartValue = CookieUtil.getCookie(request, "buyerCart");
            //}

           // System.out.println(buyerCartValue);
        }
        else{//用户登录了。则去数据库或者redis里面进行获取购物车信息。
            //利用用户名作为键名，购物车的值作为键值。

            redisUtil = (RedisUtil) SpringUtil.applicationContext.getBean("redisUtil");//从spring容器里面得到一个对象
            if(redisUtil.get(user_name)!=null){
                buyerCartValue = redisUtil.get(user_name);
            }

        }

        if(buyerCartValue!=null){//说明存在购物车字符串键值，取出来反序列化成购物车对象。
            buyerCart = JSON.parseObject(buyerCartValue, new TypeReference<BuyerCart>(){});

        }
        model.addAttribute("buyerCart", buyerCart);
        return "shopcart";
    }
    //把数据从cookie导入到redis，并显示界面。
    @RequestMapping("import_buyerCart")
    public String import_BuyerCart(HttpServletRequest request,HttpServletResponse response,
                                   Model model){
        BuyerCart buyerCart_Cookie = null;
        BuyerCart buyerCart_Redis = null;
        String buyerCartValue_Cookie = null;
        String buyerCartValue_Redis = null;
        RedisUtil redisUtil = null;
        //该方法是为了解决未登录转登录存入redis的，所以user_name必定有值
        String user_name = request.getSession().getAttribute
                ("user_name").toString();
        redisUtil = (RedisUtil) SpringUtil.applicationContext.
                getBean("redisUtil");//从spring容器里面得到一个对象
        //分别取出cookie和redis里面存的值，反序列化成购物车
        buyerCartValue_Cookie = CookieUtil.getCookie(request, "buyerCart");
        //System.out.println(buyerCartValue_Cookie);
        buyerCartValue_Redis = redisUtil.get(user_name);
        if (buyerCartValue_Redis == null){
            buyerCart_Redis = new BuyerCart();
        }
        else
            buyerCart_Redis = JSON.parseObject(buyerCartValue_Redis,
                            new TypeReference<BuyerCart>(){});
        //cookie不为空，才能导入到redis。
        if (buyerCartValue_Cookie != null){
            buyerCart_Cookie = JSON.parseObject(buyerCartValue_Cookie, new TypeReference<BuyerCart>(){});
            System.out.print(buyerCartValue_Cookie);
            //对cookie的购物车内容进行逐项比较，
            // redis购物车如果有，就修改数量，没有就插入。
            int index = -1;
            for (int i = 0; i < buyerCart_Cookie.getItems().size(); i++){
                index = -1;
                if(buyerCart_Redis.getItems() != null || buyerCart_Redis.getItems().size() > 0)
                    index = buyerCart_Redis.findItem(buyerCart_Cookie.getItems().get(i));
                if (index >= 0) {//说明redis中存在，则修改数量，并在cookie里面设置商品不存在
                    if(buyerCart_Cookie.getItems().get(i).isHave()==true&&buyerCart_Cookie.getItems().get(i).getAmount()!=0
                    &&buyerCart_Cookie.getItems().get(i).isImpo()==false){
                        buyerCart_Redis.getItems().get(index).incrementAmount(buyerCart_Cookie.getItems().get(i).getAmount());
                        buyerCart_Redis.getItems().get(index).setHave(true);
                        buyerCart_Redis.getItems().get(index).setChecked(false);

                        String fromObject1 = JSON.toJSONString(buyerCart_Redis);
                        redisUtil.set(user_name, fromObject1.toString());

                        buyerCart_Cookie.getItems().get(i).setHave(false);
                        buyerCart_Cookie.getItems().get(i).setImpo(true);
                        buyerCart_Cookie.getItems().get(i).setAmount(0);
                        String fromObject = JSON.toJSONString(buyerCart_Cookie);
                        CookieUtil.writeCookie(response, "buyerCart", fromObject);
                    }
                }
                else {//不存在，则从cookie购物车添加到redis购物车。cookie中商品存在且数量不为0，
                    if(buyerCart_Cookie.getItems().get(i).isHave()==true&&buyerCart_Cookie.getItems().get(i).getAmount()!=0&&
                    buyerCart_Cookie.getItems().get(i).isImpo()==false){
                        buyerCart_Cookie.getItems().get(i).setImpo(true);//设置为已经导入过redis

                        buyerCart_Redis.addItem(buyerCart_Cookie.getItems().get(i));

                        String fromObject1 = JSON.toJSONString(buyerCart_Redis);
                        redisUtil.set(user_name, fromObject1.toString());

                        buyerCart_Cookie.getItems().get(i).setHave(false);
                        buyerCart_Cookie.getItems().get(i).setAmount(0);
                        String fromObject = JSON.toJSONString(buyerCart_Cookie);
                        CookieUtil.writeCookie(response, "buyerCart", fromObject);
                    }
                }
            }
        }
        buyerCartValue_Redis = redisUtil.get(user_name);
        buyerCart_Redis = JSON.parseObject(buyerCartValue_Redis,
                new TypeReference<BuyerCart>(){});
        model.addAttribute("buyerCart", buyerCart_Redis);
        return "shopcart";
    }

    //用来处理ajax调用选中的item，这样结算时候直接去redsis里面取出来
    //生成订单即可。
    @ResponseBody
    @RequestMapping("set_buyerItem_check_status")
    public String setBuyerItemStatus(HttpServletRequest request){
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        //int amount = Integer.parseInt(request.getParameter("amount"));
        boolean sku_status = Boolean.parseBoolean(request.getParameter("sku_status"));
        String user_name = request.getParameter("user_name");
        RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.
                getBean("redisUtil");
        String buyerCartValue = redisUtil.get(user_name);
        BuyerCart buyerCart = JSON.parseObject(buyerCartValue,
                    new TypeReference<BuyerCart>(){});
        for (int i = 0; i < buyerCart.getItems().size(); i++){
            if (buyerCart.getItems().get(i).getSku_info().getSku_id() == sku_id){
                buyerCart.getItems().get(i).setChecked(sku_status);
                break;
            }
        }
        String fromObject = JSON.toJSONString(buyerCart);
        redisUtil.set(user_name, fromObject.toString());
        return "ajax获取数据为" + sku_id + "|" + sku_status + "|" + user_name;
    }

    @ResponseBody
    @RequestMapping("set_all_buyerItem_check_status")
    public String setAllBuyerItemStatus(HttpServletRequest request){
        String[] sku_info = request.getParameter("arr_sku_info").split("\\-");
        String[] sku_status = request.getParameter("arr_sku_status").split("\\-");

        String user_name = request.getParameter("user_name");
        RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.
                getBean("redisUtil");
        String buyerCartValue = redisUtil.get(user_name);
        BuyerCart buyerCart = JSON.parseObject(buyerCartValue,
                new TypeReference<BuyerCart>(){});
        for (int i = 0; i < sku_info.length; i++){
            for (int j = 0; j < buyerCart.getItems().size(); j++){
                if (Integer.parseInt(sku_info[i]) ==
                        buyerCart.getItems().get(j).getSku_info().getSku_id()){
                    buyerCart.getItems().get(j).setChecked(Boolean.parseBoolean(sku_status[i]));
                    break;
                }
            }
        }
        String fromObject = JSON.toJSONString(buyerCart);
        redisUtil.set(user_name, fromObject.toString());
        return "ajax获取数据为" + "|"  + "|" + user_name;
    }
}
