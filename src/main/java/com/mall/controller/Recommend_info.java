package com.mall.controller;

import com.mall.service.Recommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Recommend_info {
    @Autowired private Recommend recommendservice;


}
