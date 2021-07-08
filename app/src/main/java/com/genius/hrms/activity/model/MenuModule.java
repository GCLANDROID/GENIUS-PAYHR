package com.genius.hrms.activity.model;

public class MenuModule {
    String menuName,menuDetails;
    boolean isExpanded;
    boolean check;

    public MenuModule(String menuName, String menuDetails, boolean check) {
        this.menuName = menuName;
        this.menuDetails = menuDetails;
        this.check = check;
        isExpanded = false;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDetails() {
        return menuDetails;
    }

    public void setMenuDetails(String menuDetails) {
        this.menuDetails = menuDetails;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
