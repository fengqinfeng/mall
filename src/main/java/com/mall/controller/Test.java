package com.mall.controller;

import com.mall.entity.Pay_info;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Test {
    @RequestMapping("ttt")
    public Pay_info tttt(){
        Pay_info payinfo=new Pay_info();


        return payinfo;
    }
}
