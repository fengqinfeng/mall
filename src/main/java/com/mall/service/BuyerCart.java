package com.mall.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mall.entity.BuyerItem;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class BuyerCart{
    private List<BuyerItem> items = new ArrayList<BuyerItem>(); //一个用户只有一个购物车，一个购物车里面有多个item，所以要用到list,一个item包含一个商品
    private double totalMoney = 0;
    //用来处理购物车里面添加一个新的商品
    // （新的商品就是一个商品明细类BuyerItem的对象）。
    @JsonIgnore
    public void addItem(BuyerItem buyerItem){
        this.items.add(buyerItem);
    }


    public void removeItem(int index){
        this.items.remove(index);
    }
    public List<BuyerItem> getItems() {
        return items;
    }

    public void setItems(List<BuyerItem> items) {
        this.items = items;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }
    //还没想的很全面，目的是看购物车里面是否已经存在该商品。
    @JsonIgnore
    public int findItem(BuyerItem buyerItem){
        int index = -1;
        //此处查找比较程序有问题。
        String s1 = buyerItem.getSku_info().getUser_select_property();
        //System.out.println(s1);
        String s2 = null;
        for (int i = 0; i < this.items.size(); i++) {

            s2 = this.items.get(i).getSku_info().getUser_select_property();
            //System.out.println(s2);
            //System.out.println(s2);
            if (buyerItem.getSku_info().getSku_id() == this.items.get(i).getSku_info().getSku_id()){
                if (s1.equals(s2)){
                    //System.out.println(s2);
                    index = i;
                    //System.out.println("....." + index);
                    break;
                }
            }
        }
        return index;
    }
}
