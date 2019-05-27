package com.mall.entity;

public class Address_info {
    private int address_id;
    private int user_id;
    private String address;
    private String linkman;
    private String telephone;
    private int defaultAddress;

    public Address_info(int address_id, int user_id, String address, String linkman, String telephone, int defaultAddress) {
        this.address_id = address_id;
        this.user_id = user_id;
        this.address = address;
        this.linkman = linkman;
        this.telephone = telephone;
        this.defaultAddress = defaultAddress;
    }
    public Address_info() {
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(int defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
