package com.genius.hrms.activity.model;


import com.google.gson.annotations.SerializedName;

/**
 * Created by LENOVO on 8/21/2018.
 */

public class AttendanceManageModule {
    @SerializedName("AEMPEmployeeID")
    String AEMPEmployeeID;
    @SerializedName("responseText")
   public String responseText;
   @SerializedName("responseStatus")
    public boolean responseStatus;

    public AttendanceManageModule(String responseText, boolean responseStatus) {
        this.responseText = responseText;
        this.responseStatus = responseStatus;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public boolean isResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(boolean responseStatus) {
        this.responseStatus = responseStatus;
    }
}
