package com.mall.service;

import com.mall.entity.Ratings_info;
import com.mall.mapper.Ratings_Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Ratings_Service {
    @Autowired
    Ratings_Mapper ratingsmapper;

    public List<Ratings_info>selectall(){
        return ratingsmapper.selectall();
    }
    public int insertratings(Ratings_info ratings){
        return ratingsmapper.insertRatings(ratings);
    }
}
