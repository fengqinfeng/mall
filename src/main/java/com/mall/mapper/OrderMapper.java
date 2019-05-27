package com.mall.mapper;

import com.mall.entity.Order_info;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    int insertOrder(Order_info order_info);
    List<Order_info> selOrder_info(int user_id, String queryCondition);

}
