package com.mall.controller;

import com.mall.entity.Pay_info;
import com.mall.entity.Ratings_info;
import com.mall.service.Ratings_Service;
import com.mall.util.GetTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

@Controller
public class Test {
    @Autowired
    Ratings_Service ratingservice;
    @RequestMapping("ttt")
    public String tttt(){
        GetTime ordernumber=new GetTime();

        Ratings_info ans=new Ratings_info();

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

            Random random = new Random();
            for(int i=0;i<4000;i++){
                int nums;
                nums = Math.round(random.nextInt(10)+1);
                ans.setUser_id(nums);
                bw.write(Integer.toString(nums));
                bw.write(",");
                nums=Math.round(random.nextInt(16)+1);
                ans.setItem_id(nums);
                bw.write(Integer.toString(nums));
                bw.write(",");

                nums=Math.round(random.nextInt(5)+1);
                ans.setRating(nums);
                bw.write(Integer.toString(nums));
                bw.newLine();
                String temp=ordernumber.getGuid();
                ans.setTimestamp(temp);
                ratingservice.insertratings(ans);
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }






        return "test";
    }
}
