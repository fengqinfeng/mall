package com.mall.entity;

import java.util.List;

//一个库存商品的具体信息，包括商品名称，各种型号等信息。
public class Sku_info {
    private boolean checked;//商品是否被选中
    private int buyAmount;//买家买的数量，数据库读不出来，来自界面输入
    private int sale_number;
    private int product_class_id;
    private String product_class_name;
    private int sku_id;
    private int amount;
    private String p_image_path;//商品的图片相对路径如：/image/bjb.jpg
    //以下2个字段表示的是seller_info表和sku_info表是1 vs n的关系，真实
    //淘宝店应该是n vs m的关系，需要在加上一张中间表来处理。这里简化处理了。
    private int seller_id;
    private String seller_name;
    private String class_id;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public void incrementAmount(int buyAmount){
        this.buyAmount += buyAmount;
    }
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(int buyAmount) {
        this.buyAmount = buyAmount;
    }

    public int getSale_number() {
        return sale_number;
    }

    public void setSale_number(int sale_number) {
        this.sale_number = sale_number;
    }

    //用户购买商品时选择的组合属性,合并后用|分割，不能从数据库中读取。
    // 写到一个字段里面，如：绿色|XXL这样的格式
    private String user_select_property;
    //private String property_name;
    private double price;
    private List<Product_property_info> product_property_infoList;


    public Sku_info() {
    }

    public int getProduct_class_id() {
        return product_class_id;
    }

    public void setProduct_class_id(int product_class_id) {
        this.product_class_id = product_class_id;
    }

    public String getProduct_class_name() {
        return product_class_name;
    }

    public void setProduct_class_name(String product_class_name) {
        this.product_class_name = product_class_name;
    }

    public int getSku_id() {
        return sku_id;
    }

    public void setSku_id(int sku_id) {
        this.sku_id = sku_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getP_image_path() {
        return p_image_path;
    }

    public void setP_image_path(String p_image_path) {
        this.p_image_path = p_image_path;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getUser_select_property() {
        return user_select_property;
    }

    public void setUser_select_property(String user_select_property) {
        this.user_select_property = user_select_property;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Product_property_info> getProduct_property_infoList() {
        return product_property_infoList;
    }

    public void setProduct_property_infoList(List<Product_property_info> product_property_infoList) {
        this.product_property_infoList = product_property_infoList;
    }
}
