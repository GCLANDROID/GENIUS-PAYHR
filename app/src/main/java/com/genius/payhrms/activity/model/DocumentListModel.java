package com.genius.payhrms.activity.model;

public class DocumentListModel {

    int HRManualID;
    String hrDescription;

    public DocumentListModel(int HRManualID, String hrDescription) {
        this.HRManualID = HRManualID;
        this.hrDescription = hrDescription;
    }

    public int getHRManualID() {
        return HRManualID;
    }

    public void setHRManualID(int HRManualID) {
        this.HRManualID = HRManualID;
    }

    public String getHrDescription() {
        return hrDescription;
    }

    public void setHrDescription(String hrDescription) {
        this.hrDescription = hrDescription;
    }
}
