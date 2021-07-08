package com.genius.hrms.activity.model;

import java.io.Serializable;

public class MulFenceModel  {
    String lattitude,longtitude;

    public MulFenceModel(String lattitude, String longtitude) {
        this.lattitude = lattitude;
        this.longtitude = longtitude;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
}
