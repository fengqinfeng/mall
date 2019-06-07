package com.mall.entity;

import java.util.ArrayList;
import java.util.List;

public class Order_info {
    private List<Order_item_info> order_item_infoList = new ArrayList<Order_item_info>();
    private String order_number;
    private int order_id;
    private String order_time;
    private int seller_id;
    private String seller_name;
    private int user_id;
    private double allmoney;
    private int order_status;
    private String linkman;
    private String telephone;
    private String address;
    private String pya_date;
    private String send_date;
    private String end_date;
    private String user_say;//表示是否付钱
    private String user_evaluation;//表示是否评论
    private int cancel_status;//1表示取消订单，交易关闭

    public int getCancel_status() {
        return cancel_status;
    }

    public void setCancel_status(int cancel_status) {
        this.cancel_status = cancel_status;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public List<Order_item_info> getOrder_item_infoList() {
        return order_item_infoList;
    }

    public void setOrder_item_infoList(List<Order_item_info> order_item_infoList) {
        this.order_item_infoList = order_item_infoList;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getAllmoney() {
        return allmoney;
    }

    public void setAllmoney(double allmoney) {
        this.allmoney = allmoney;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPya_date() {
        return pya_date;
    }

    public void setPya_date(String pya_date) {
        this.pya_date = pya_date;
    }

    public String getSend_date() {
        return send_date;
    }

    public void setSend_date(String send_date) {
        this.send_date = send_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getUser_say() {
        return user_say;
    }

    public void setUser_say(String user_say) {
        this.user_say = user_say;
    }

    public String getUser_evaluation() {
        return user_evaluation;
    }

    public void setUser_evaluation(String user_evaluation) {
        this.user_evaluation = user_evaluation;
    }
}
