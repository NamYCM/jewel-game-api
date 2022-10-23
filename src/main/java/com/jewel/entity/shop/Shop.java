package com.jewel.entity.shop;

import java.util.Map;

public class Shop {
    Map<String, ShopItem> shopItems;


    public Map<String, ShopItem> getShopItems() {
        return shopItems;
    }

    public void setShopItems(Map<String, ShopItem> shopItems) {
        this.shopItems = shopItems;
    }
}
