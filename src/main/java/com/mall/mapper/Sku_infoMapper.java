package com.mall.mapper;

import com.mall.entity.Sku_info;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Sku_infoMapper {
    public Sku_info showSku_info_Detail(int sku_id);//界面显示
    public Sku_info getSingleSku_info(int sku_id);//购物车显示
    public List<Sku_info> qry_Sku_info(@Param("0")String class_id,
                                       @Param("sort")String sort);
    public int updateSale_number(@Param("0")int sale_number,
                                 @Param("1")int sku_id);
    public int selSale_Number(@Param("0")int sku_id);
}
