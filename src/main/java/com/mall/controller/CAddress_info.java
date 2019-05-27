package com.mall.controller;
import com.mall.entity.Address_info;
import java.util.ArrayList;
import java.util.List;
import com.mall.service.Address_infoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
@RestController
public class CAddress_info {
    @Autowired
    private Address_infoService address_infoService;
    @RequestMapping("allAddress")
    public List<Address_info> selAllAddress(){
        return address_infoService.selAllAddress(1);
    }
    @RequestMapping("insertAddress")
    public int insertAddress(HttpServletRequest request){
        Address_info address_info = new Address_info();
        address_info.setAddress("南京市江宁区五一路45号鸡鸣城5-1-1909");
        address_info.setUser_id(5);
        address_info.setTelephone("18911115678");
        address_info.setLinkman("达摩祖师");
        //address_info.setUser_id(Integer.parseInt(request.getParameter("user_id")));

        return  address_infoService.insertAddress(address_info);
    }

    @RequestMapping("updateAddress")
    public int updateAddress(){
        Address_info address_info =
                new Address_info(1, 1,
                        "南京市江宁区五一路45号鸡鸣城5-1-1903",
                        "修晓杰","13073660993",
                        1);
        return address_infoService.updateAddress(address_info);
    }
}
