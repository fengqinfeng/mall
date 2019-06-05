package com.mall.mapper;
import com.mall.entity.Address_info;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface Address_infoMapper {
    public List<Address_info> selAllAddress(int user_id);
    public Address_info selDefaultAddress(int user_id);
    public int insertAddress(Address_info address_info);
    public int updateAddress(Address_info address_info);
    public Address_info selOneAddress(int user_id,String address);
}
