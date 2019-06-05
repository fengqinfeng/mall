package com.mall.service;

import com.mall.entity.*;
import com.mall.mapper.OrderMapper;
import com.mall.mapper.Order_itemMapper;
import com.mall.mapper.Sku_infoMapper;
import com.mall.util.GetTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderService {
    @Autowired OrderMapper orderMapper;
    @Autowired Order_itemMapper order_itemMapper;
    @Autowired Sku_infoMapper sku_infoMapper;

    public Order_item_info orderiteminfo(int itemid){
        return order_itemMapper.selectitem(itemid);
    }
    public List<Order_info> showorderbysql(int user_id){
        return orderMapper.selOrder_info(user_id);
    }

    public int itemupdate(int itemid){
        return order_itemMapper.updateeva(itemid);
    }


    public int maxid(){
        return orderMapper.maxid();
    }
//    public List<Order_info> showOrder(int user_id, String queryCondition){
//        return orderMapper.showOrder(user_id, queryCondition);
//    }

    //生成订单
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT, readOnly = false)
    public int InsertOrder_Patch(List<BuyerItem_Patch>
              item_patchList, int user_id, Address_info address_info){

        GetTime ordernumber=new GetTime();
        Order_info order_info = new Order_info();
        for (int i = 0; i < item_patchList.size(); i++){
            //对每一个卖家分别生成订单。并插入总的订单
            SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date ans=new Date();
            order_info.setOrder_status(1);
            order_info.setSeller_id(item_patchList.get(i).getSeller_id());
            order_info.setOrder_time(f.format(ans));
            order_info.setUser_id(user_id);
            order_info.setAddress(address_info.getAddress());
            order_info.setLinkman(address_info.getLinkman());
            order_info.setTelephone(address_info.getTelephone());

            //随机生成20位订单号
            order_info.setOrder_number(ordernumber.getGuid());
            int orderid=maxid();
            double allm=0;
            for (int j = 0; j < item_patchList.get(i).getSku_infoList().size(); j++){
                //对每一个明细进行插入。
                Sku_info sku_info = item_patchList.get(i).getSku_infoList().get(j);
                Order_item_info order_item_info = new Order_item_info();
                order_item_info.setOrder_id(orderid+1);
                order_item_info.setSku_id(sku_info.getSku_id());
                order_item_info.setPrice(sku_info.getPrice());
                order_item_info.setAmount(sku_info.getBuyAmount());
                order_item_info.setUser_select_property(sku_info.getUser_select_property());
                order_item_info.setUser_eva(0);
                order_itemMapper.insertOrderItem(order_item_info);
                allm+=sku_info.getPrice()*sku_info.getBuyAmount();
                //修改sku_info的sale_number
                int sku_id = order_item_info.getSku_id();
                int sale_number = sku_infoMapper.selSale_Number(sku_id) + 1;
                sku_infoMapper.updateSale_number(sale_number,sku_id);
            }
            order_info.setAllmoney(allm);
            order_info.setUser_evaluation("0");
            order_info.setUser_say("0");
            orderMapper.insertOrder(order_info);
        }
        //删除购物车添加到订单的商品
        /*Iterator<BuyerItem> it = null;//item_patchList.iterator();
        while (it.hasNext()) {
            if (it.next().isChecked())
                it.remove();
        }*/

        return 0;
    }
}
