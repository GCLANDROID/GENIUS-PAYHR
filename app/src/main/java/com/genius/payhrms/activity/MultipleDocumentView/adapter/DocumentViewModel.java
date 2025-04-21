package com.genius.payhrms.activity.MultipleDocumentView.adapter;

public class DocumentViewModel {
    String name;
    String base64String;
    String type;

    public DocumentViewModel(String name, String base64String, String type) {
        this.name = name;
        this.base64String = base64String;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


