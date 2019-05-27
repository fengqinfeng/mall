package com.mall.entity;

public class Product_property_info {
    private int property_id;
    private String product_class_id;
    private String property_name;
    private String property_value;
    private int is_sale_property;
    private int sku_id;

    public Product_property_info(int property_id, String product_class_id, String property_name, String property_value, int is_sale_property, int sku_id) {
        this.property_id = property_id;
        this.product_class_id = product_class_id;
        this.property_name = property_name;
        this.property_value = property_value;
        this.is_sale_property = is_sale_property;
        this.sku_id = sku_id;
    }

    public Product_property_info() {
    }

    public int getProperty_id() {
        return property_id;
    }

    public void setProperty_id(int property_id) {
        this.property_id = property_id;
    }

    public String getProduct_class_id() {
        return product_class_id;
    }

    public void setProduct_class_id(String product_class_id) {
        this.product_class_id = product_class_id;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getProperty_value() {
        return property_value;
    }

    public void setProperty_value(String property_value) {
        this.property_value = property_value;
    }

    public int getIs_sale_property() {
        return is_sale_property;
    }

    public void setIs_sale_property(int is_sale_property) {
        this.is_sale_property = is_sale_property;
    }

    public int getSku_id() {
        return sku_id;
    }

    public void setSku_id(int sku_id) {
        this.sku_id = sku_id;
    }
}
