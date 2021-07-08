package com.genius.hrms.activity.model;

public class ELearningModel {
    String FileLabel,ImageUrl;

    public ELearningModel(String fileLabel, String imageUrl) {
        FileLabel = fileLabel;
        ImageUrl = imageUrl;
    }

    public String getFileLabel() {
        return FileLabel;
    }

    public void setFileLabel(String fileLabel) {
        FileLabel = fileLabel;
    }


    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
