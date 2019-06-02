package com.mall.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Te {
    public static void main(String args[]){
        //获得路径
        //String filepath = System.getProperty("user.dir");
        //filepath += "\\file.txt";

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
            for(int i=0;i<2000;i++){
                int nums;
                nums = Math.round(random.nextInt(10)+1);
                bw.write(Integer.toString(nums));
                bw.write(",");
                nums=Math.round(random.nextInt(12)+1);
                bw.write(Integer.toString(nums));
                bw.write(",");

                nums=Math.round(random.nextInt(4)+1);
                bw.write(Integer.toString(nums));
//                bw.write(",");
//                nums=Math.round(random.nextInt(90000)+1);
//                bw.write(Integer.toString(nums));
                bw.newLine();
            }
//            for(int i=0;i<10000;i++){
//                int nums = Math.round(random.nextFloat()*1000.0f);
//                //将int 转化为 String类型
//                bw.write(Integer.toString(nums));
//                bw.newLine();
//            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
