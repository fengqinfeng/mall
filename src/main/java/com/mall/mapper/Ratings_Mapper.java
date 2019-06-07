package com.mall.mapper;

import com.mall.entity.Ratings_info;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Ratings_Mapper {
    public int insertRatings(Ratings_info ratings);
    public List<Ratings_info> selectall();
}
