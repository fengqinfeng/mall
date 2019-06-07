package com.mall.mapper;

import com.mall.entity.Order_info;
import com.mall.entity.Order_item_info;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Order_itemMapper {
    int insertOrderItem(Order_item_info order_item_info);
    List<Order_info> selOrder_info(int user_id);
    public int updateeva(int order_item_id);
    public Order_item_info selectitem(int order_item_id);
    public List<Order_item_info> seleorderid(int order_id);
}
