package com.mall.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.scheduling.support.TaskUtils;

import java.io.Serializable;
import java.util.Objects;
//购物车明细类，就是购物车里面包含的商品信息和数量
public class BuyerItem {
    //通过传入的具体商品主键id获取所需要的商品信息。
    private Sku_info sku_info;
    private boolean isHave = true;  //是否删除改商品，true表示商品存在
    private int amount = 0;
    private boolean checked = false;//登录的时候，表示加到订单时是否选中
    private boolean impo=false;//是否导入redis

    public boolean isImpo() {
        return impo;
    }

    public void setImpo(boolean impo) {
        this.impo = impo;
    }

    public Sku_info getSku_info() {
        return sku_info;
    }

    public void setSku_info(Sku_info sku_info) {
        this.sku_info = sku_info;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isHave() {
        return isHave;
    }

    public void setHave(boolean have) {
        isHave = have;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    //用来处理添加的商品，在购物车里面已经存在，则直接修改数量。
    @JsonIgnore
    public void incrementAmount(int amount){
        this.amount += amount;
    }

    public BuyerItem() {
    }

    public BuyerItem(Sku_info sku_info, boolean isHave, int amount) {
        this.sku_info = sku_info;
        this.isHave = isHave;
        this.amount = amount;
    }
}
