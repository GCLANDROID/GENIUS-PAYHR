package com.genius.hrms.activity.model;

public class SpinnerModel {
    String item,itemId;

    public SpinnerModel(String item, String itemId) {
        this.item = item;
        this.itemId = itemId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
