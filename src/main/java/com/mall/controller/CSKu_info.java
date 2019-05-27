package com.mall.controller;

import com.mall.entity.Sku_info;
import com.mall.service.Sku_infoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

//@Controller
@RestController
public class CSKu_info {
    /*
    @Autowired
    private Sku_infoService sku_infoService;
    @RequestMapping("show_sku_info")
    public Sku_info show_Sku_info(HttpServletRequest request){
        //int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        return sku_infoService.showSku_info_Detail(1);
    }
    @RequestMapping("show_single_sku_info")
    public Sku_info show_Single_Sku_info(HttpServletRequest request){
        //int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        return sku_infoService.getSingleSku_info(1);
    }*/
}
