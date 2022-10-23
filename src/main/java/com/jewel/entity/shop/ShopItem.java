package com.jewel.entity.shop;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopItem {
    @JsonProperty("IconPath")
    String IconPath;
    @JsonProperty("Price")
    int Price;

    public String getIconPath() {
        return IconPath;
    }

    public void setIconPath(String iconPath) {
        IconPath = iconPath;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
