package com.mall.mapper;

import com.mall.entity.Ratings_info;
import org.springframework.stereotype.Repository;

@Repository
public interface Ratings_Mapper {
    public int insertRatings(Ratings_info ratings);
}
