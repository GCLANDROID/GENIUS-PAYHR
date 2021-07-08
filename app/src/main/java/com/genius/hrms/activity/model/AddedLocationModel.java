package com.genius.hrms.activity.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AddedLocationModel implements Parcelable {
    String loaction;

    protected AddedLocationModel(Parcel in) {
        loaction = in.readString();
    }

    public static final Creator<AddedLocationModel> CREATOR = new Creator<AddedLocationModel>() {
        @Override
        public AddedLocationModel createFromParcel(Parcel in) {
            return new AddedLocationModel(in);
        }

        @Override
        public AddedLocationModel[] newArray(int size) {
            return new AddedLocationModel[size];
        }
    };

    public String getLoaction() {
        return loaction;
    }

    public void setLoaction(String loaction) {
        this.loaction = loaction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(loaction);
    }

    public AddedLocationModel(String loaction) {
        this.loaction = loaction;
    }
}
