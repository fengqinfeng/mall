package com.mall.entity;
import java.util.ArrayList;
import java.util.List;
public class BuyerItem_Patch {
    //单个订单里面包含>=1个商品，这里一个卖家一个订单
    private List<Sku_info> sku_infoList=new ArrayList<Sku_info>();
    private boolean isHave = true;//true表示订单存在
  private int amount = 0;
    private boolean checked = false;//false表示订单未导入到mysql
    private int seller_id;
    private String seller_name;


    public List<Sku_info> getSku_infoList() {
        return sku_infoList;
    }
    public void setSku_infoList(List<Sku_info> sku_infoList) {
        this.sku_infoList = sku_infoList;
    }
    public void addSku_info(Sku_info sku_info){
        this.sku_infoList.add(sku_info);
    }
    public void removeSku_info(Sku_info sku_info){
        this.sku_infoList.remove(sku_info);
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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
}
