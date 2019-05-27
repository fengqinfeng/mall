package com.mall.service;

import com.mall.entity.*;
import com.mall.mapper.OrderMapper;
import com.mall.mapper.Order_itemMapper;
import com.mall.mapper.Sku_infoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderService {
    @Autowired OrderMapper orderMapper;
    @Autowired Order_itemMapper order_itemMapper;
    @Autowired Sku_infoMapper sku_infoMapper;

//    public List<Order_info> showOrder(int user_id, String queryCondition){
//        return orderMapper.showOrder(user_id, queryCondition);
//    }
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT, readOnly = false)
    public int InsertOrder_Patch(List<BuyerItem_Patch>
              item_patchList, int user_id, Address_info address_info){
        for (int i = 0; i < item_patchList.size(); i++){
            //对每一个卖家分别生成订单。并插入总的订单
            Order_info order_info = new Order_info();
            order_info.setUser_id(user_id);
            order_info.setAddress(address_info.getAddress());
            order_info.setLinkman(address_info.getLinkman());
            order_info.setTelephone(address_info.getTelephone());
            //随机生成20位订单号。。。代码没写。
            order_info.setOrder_number("12345678900987654321");
            orderMapper.insertOrder(order_info);
            for (int j = 0; j < item_patchList.get(i).
                    getSku_infoList().size(); j++){
                //对每一个明细进行插入。
                Sku_info sku_info = item_patchList.get(i).getSku_infoList().get(j);
                Order_item_info order_item_info = new Order_item_info();
                order_item_info.setOrder_id(order_info.getOrder_id());
                order_item_info.setSku_id(sku_info.getSku_id());
                order_item_info.setPrice(sku_info.getPrice());
                order_item_info.setAmount(sku_info.getBuyAmount());
                order_item_info.setUser_select_property(sku_info.getUser_select_property());
                order_itemMapper.insertOrderItem(order_item_info);
                //修改sku_info的sale_number
                int sku_id = order_item_info.getSku_id();
                int sale_number = sku_infoMapper.selSale_Number(sku_id) + 1;
                sku_infoMapper.updateSale_number(sale_number,sku_id);
            }
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
