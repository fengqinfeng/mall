package com.mall.service;

import com.mall.entity.Class_info;
import com.mall.mapper.Class_infoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Class_infoService {
    @Autowired
    private Class_infoMapper class_infoMapper;
    public List<Class_info> class_all(){
        List<Class_info>ans=class_infoMapper.selectall();
        if(ans!=null)return ans;
        else return null;

    }
}
