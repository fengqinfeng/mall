package com.mall.controller;

import com.mall.entity.Ratings_info;
import com.mall.service.Ratings_Service;
import com.mall.service.Recommend;
import com.mall.util.GetTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class Recommend_info {
    @Autowired public Ratings_Service raservice;

    @RequestMapping("outpu")
    public String outpu(HttpServletRequest request, HttpServletResponse response, Model model){
        List<Ratings_info>ans=new ArrayList<Ratings_info>();
        ans=raservice.selectall();
        GetTime ordernumber=new GetTime();
        File file = new File("C:\\D\\课件\\企业级javaee\\推荐算法学习\\ml-latest-small\\ra.csv");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for(int i=0;i<ans.size();i++){
                bw.write(Integer.toString(ans.get(i).getUser_id()));
                bw.write(",");
                bw.write(Integer.toString(ans.get(i).getItem_id()));
                bw.write(",");
                bw.write(Integer.toString(ans.get(i).getRating()));
                bw.write(",");
                bw.write(ordernumber.getGuid());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";

    }



}
