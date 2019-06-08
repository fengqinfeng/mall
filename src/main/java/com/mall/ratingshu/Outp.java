package com.mall.ratingshu;

import com.mall.entity.Ratings_info;
import com.mall.service.Ratings_Service;
import com.mall.util.GetTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Outp {

    @Autowired
    public Ratings_Service raservice;

    //每天凌晨执行，导出评分数据到文件中
    @Scheduled(cron = "0 0 0 * * *")
    public void work() {
        List<Ratings_info> ans=new ArrayList<Ratings_info>();
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
    }
}
