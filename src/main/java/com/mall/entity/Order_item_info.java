package com.mall.entity;

public class Order_item_info {
    private int order_item_id;
    private int order_id;
    private int sku_id;
    private double price;
    private int amount;
    private String user_select_property;

    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getSku_id() {
        return sku_id;
    }

    public void setSku_id(int sku_id) {
        this.sku_id = sku_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUser_select_property() {
        return user_select_property;
    }

    public void setUser_select_property(String user_select_property) {
        this.user_select_property = user_select_property;
    }
}
