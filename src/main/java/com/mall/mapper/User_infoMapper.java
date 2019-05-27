package com.mall.mapper;
import com.mall.entity.User_info;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface User_infoMapper {
    public User_info selUser_info(int user_id);
    public List<User_info> selAll();
    public User_info selAllAddressOfUser(int user_id);
    public int checkLogin(User_info user_info);
    public User_info checkLoginMultiParam(@Param("0") String user_name,
                                    @Param("1") String pwd);
}
