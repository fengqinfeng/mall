package com.mall.service;
import com.mall.entity.Address_info;
import com.mall.mapper.Address_infoMapper;
import com.sun.javaws.exceptions.ErrorCodeResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class Address_infoService {
    @Autowired
    private Address_infoMapper address_infoMapper;
    //事务的例子
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
    public int insertAddress(Address_info address_info){
        int flag = 0;
        //Address_info address_info2 = new Address_info(1, 1, "南京市江宁区五一路45号鸡鸣城5-1-1904", "修晓杰dfdsjfhskfhkfhskfskfjhskfhskfsfkshfkh2","13073660993", 1);
        flag = address_infoMapper.insertAddress(address_info);
        //flag = address_infoMapper.updateAddress(address_info2);
        return  flag;
    }
    public int updateAddress(Address_info address_info){
        return address_infoMapper.updateAddress(address_info);
    }
    public List<Address_info> selAllAddress(int user_id){
        return address_infoMapper.selAllAddress(user_id);
    }
    public Address_info selDefaultAddress(int user_id){
        return address_infoMapper.selDefaultAddress(user_id);
    }
}
