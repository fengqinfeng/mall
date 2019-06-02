package com.mall.entity;

public class Order_item_info {
    private int order_item_id;
    private int order_id;
    private int sku_id;
    private double price;
    private int amount;
    private String user_select_property;
    private String product_class_name;
    private String p_image_path;
    private int user_eva;

    public int getUser_eva() {
        return user_eva;
    }

    public void setUser_eva(int user_eva) {
        this.user_eva = user_eva;
    }

    public String getP_image_path() {
        return p_image_path;
    }

    public void setP_image_path(String p_image_path) {
        this.p_image_path = p_image_path;
    }

    public String getProduct_class_name() {
        return product_class_name;
    }

    public void setProduct_class_name(String product_class_name) {
        this.product_class_name = product_class_name;
    }

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
