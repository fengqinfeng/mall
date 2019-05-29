package com.mall;

import com.mall.util.GetTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args){
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date ans=new Date();
        System.out.println(f.format(ans));
        System.out.println(GetTime.getGuid());
    }
}
