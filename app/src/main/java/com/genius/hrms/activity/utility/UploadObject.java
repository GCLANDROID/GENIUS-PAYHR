package com.genius.hrms.activity.utility;

import com.google.gson.annotations.SerializedName;

/**
 * Created by robert on 8/16/17.
 */

public class UploadObject {
    @SerializedName("AEMPEmployeeID")
    String AEMPEmployeeID;
    @SerializedName("responseText")
    public String responseText;
    @SerializedName("responseStatus")
    public boolean responseStatus;
    //responseStatus



    public UploadObject(String responseText, boolean responseStatus) {
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

