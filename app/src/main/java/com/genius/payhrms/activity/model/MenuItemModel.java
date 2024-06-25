package com.genius.payhrms.activity.model;

public class MenuItemModel {
    String menuName;
    int menuId;

    public MenuItemModel(String menuName, int menuId) {
        this.menuName = menuName;
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
}
