package com.genius.payhrms.activity.model;
import android.net.Uri;
public class MultipleDocModel {
    String fileName;
    String fileType;
    Uri fileUri;
    String base64String;

    public MultipleDocModel(String fileName, String fileType, Uri fileUri, String base64String) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileUri = fileUri;
        this.base64String = base64String;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }
}
