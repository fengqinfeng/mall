package com.mall.service;

import com.mall.entity.Sku_info;
import com.mall.mapper.Sku_infoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Sku_infoService {
    @Autowired
    Sku_infoMapper sku_infoMapper;
    public int updateku(Sku_info skuinfo){
        return sku_infoMapper.updateku(skuinfo);
    }
    public Sku_info seleall(int sku_id){
        return sku_infoMapper.seleall(sku_id);
    }

    public Sku_info showSku_info_Detail(int sku_id){
        return sku_infoMapper.showSku_info_Detail(sku_id);
    }
    public Sku_info getSingleSku_info(int sku_id){
        return sku_infoMapper.getSingleSku_info(sku_id);
    }

    public List<Sku_info> qry_Sku_info(String class_id,
                                       String sort){
        return sku_infoMapper.qry_Sku_info
                (class_id, sort);
    }
}