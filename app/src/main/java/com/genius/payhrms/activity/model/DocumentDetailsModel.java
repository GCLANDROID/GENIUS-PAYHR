package com.genius.payhrms.activity.model;

public class DocumentDetailsModel {

    String ManualCategory;
    String ManualSubCategory;
    String ManualDescription;
    String documentPath;

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

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }
}
