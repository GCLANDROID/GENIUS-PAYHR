package com.genius.hrms.activity.model;

public class DocumentDetailsModel {

    String ManualCategory;
    String ManualSubCategory;
    String ManualDescription;

    public DocumentDetailsModel(String manualCategory, String manualSubCategory, String manualDescription) {
        ManualCategory = manualCategory;
        ManualSubCategory = manualSubCategory;
        ManualDescription = manualDescription;
    }

    public String getManualCategory() {
        return ManualCategory;
    }

    public void setManualCategory(String manualCategory) {
        ManualCategory = manualCategory;
    }

    public String getManualSubCategory() {
        return ManualSubCategory;
    }

    public void setManualSubCategory(String manualSubCategory) {
        ManualSubCategory = manualSubCategory;
    }

    public String getManualDescription() {
        return ManualDescription;
    }

    public void setManualDescription(String manualDescription) {
        ManualDescription = manualDescription;
    }
}
