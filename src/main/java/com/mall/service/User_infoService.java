package com.mall.service;
import com.mall.entity.User_info;
import com.mall.mapper.User_infoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
@Service
public class User_infoService {
    @Autowired
    User_infoMapper user_infoMapper;
    public User_info selUser_info(int id){
        System.out.println(id);
        return user_infoMapper.selUser_info(id);
    }

    public List<User_info> selAll(){
        return user_infoMapper.selAll();
    }
    public User_info selAllAddressOfUser(int user_id){
        return user_infoMapper.selAllAddressOfUser(user_id);
    }
    public int checkLogin(User_info user_info){
        return user_infoMapper.checkLogin(user_info);
    }

    public User_info checkLoginMultiParam(String user_name, String pwd){
        return user_infoMapper.checkLoginMultiParam(user_name, pwd);
    }

}
