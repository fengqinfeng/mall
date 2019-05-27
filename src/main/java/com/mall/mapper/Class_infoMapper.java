package com.mall.mapper;

import com.mall.entity.Class_info;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Class_infoMapper {
    public List<Class_info> selectall();
}
