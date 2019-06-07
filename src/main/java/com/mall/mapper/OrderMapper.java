package com.mall.mapper;

import com.mall.entity.Order_info;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    public int insertOrder(Order_info order_info);
    public List<Order_info> selOrder_info(int user_id);
    public int maxid();
    public int payed(int order_id,String user_say);
    public List<Order_info> orderinfo_payed(int user_id);
    public int ordercancel(int order_id);
    public int orderdele(int order_id);
}
