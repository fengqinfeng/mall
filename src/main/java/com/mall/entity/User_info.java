package com.mall.entity;

import java.util.List;

public class User_info {
    private int user_id;
    private String pwd;
    private String user_name;
    private String user_real_name;
    private String telephone;
    private String card_id;
    private String user_pro;
    private List<Address_info> address_infoList;

    public User_info() {
    }

    public User_info(int user_id, String pwd, String user_name, String user_real_name, String telephone, String card_id, String user_pro) {
        this.user_id = user_id;
        this.pwd = pwd;
        this.user_name = user_name;
        this.user_real_name = user_real_name;
        this.telephone = telephone;
        this.card_id = card_id;
        this.user_pro = user_pro;
    }


    public List<Address_info> getAddress_infoList() {
        return address_infoList;
    }

    public void setAddress_infoList(List<Address_info> address_infoList) {
        this.address_infoList = address_infoList;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_real_name() {
        return user_real_name;
    }

    public void setUser_real_name(String user_real_name) {
        this.user_real_name = user_real_name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getUser_pro() {
        return user_pro;
    }

    public void setUser_pro(String user_pro) {
        this.user_pro = user_pro;
    }
}
