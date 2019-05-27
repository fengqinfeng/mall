package com.mall.service;

import com.mall.entity.BuyerItem_Patch;

import java.util.ArrayList;
import java.util.List;

public class BuyerCart_Patch {
    //一个用户的订单list
    private List<BuyerItem_Patch> items =
            new ArrayList<BuyerItem_Patch>();
    public List<BuyerItem_Patch> getItems() {
        return items;
    }
    public void setItems(List<BuyerItem_Patch> items) {
        this.items = items;
    }
    public void removeItem(int index){
        this.items.remove(index);
    }
    public void addItem(BuyerItem_Patch buyerItem){
        this.items.add(buyerItem);
    }
}
