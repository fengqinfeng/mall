package com.mall.controller;

import com.mall.entity.Ratings_info;
import com.mall.entity.Sku_info;
import com.mall.service.Ratings_Service;
import com.mall.service.Recommend;
import com.mall.service.Sku_infoService;
import com.mall.util.GetTime;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
    @Autowired
    private Sku_infoService sku_infoService;
    @Autowired
    private Recommend recommendservice;


    @ResponseBody
    @RequestMapping("tuijian")

    public List<Sku_info> tuijian(HttpServletRequest request,HttpServletResponse response){

        int userid=0;
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        userid = Integer.parseInt(request.getParameter("user_id"));
        List<Sku_info>recommendans=new ArrayList<Sku_info> ();;
        try {
            List<RecommendedItem> ans=new ArrayList<>();
            if(userid==0){
                ans=recommendservice.recommend(1,sku_id);
            }
            else{
                ans=recommendservice.recommend(userid,sku_id);
            }

            for (int i=0;i<ans.size();i++){
                //System.out.println("id="+ans.get(i).getItemID()+" value="+ans.get(i).getValue());
                Sku_info temp=sku_infoService.showSku_info_Detail((int)ans.get(i).getItemID());
                recommendans.add(temp);
            }
            //System.out.println(recommendans);
            for(int i=0;i<recommendans.size();i++){
                //System.out.println(recommendans.get(i).getSku_id()+" "+recommendans.get(i).getP_image_path());
            }
            //model.addAttribute("recommendans",recommendans);
        }catch (Exception e){
            System.out.println(e);
        }
        return recommendans;

    }


    //输出（已作废）
//    @RequestMapping("outpu")
//    public String outpu(HttpServletRequest request, HttpServletResponse response, Model model){
//        List<Ratings_info>ans=new ArrayList<Ratings_info>();
//        ans=raservice.selectall();
//        GetTime ordernumber=new GetTime();
//        File file = new File("C:\\D\\课件\\企业级javaee\\推荐算法学习\\ml-latest-small\\ra.csv");
//        if(!file.exists()){
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
//            for(int i=0;i<ans.size();i++){
//                bw.write(Integer.toString(ans.get(i).getUser_id()));
//                bw.write(",");
//                bw.write(Integer.toString(ans.get(i).getItem_id()));
//                bw.write(",");
//                bw.write(Integer.toString(ans.get(i).getRating()));
//                bw.write(",");
//                bw.write(ordernumber.getGuid());
//                bw.newLine();
//            }
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "ok";
//
//    }



}
