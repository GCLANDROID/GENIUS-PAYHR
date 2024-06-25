package com.genius.payhrms.activity.model;

public class Notificationmodel {
   String location,in,out,notmark,date,inPer,outper,notmarkPer,totalEmp,branchID;

    public Notificationmodel(String location, String in, String out, String notmark, String date, String inPer, String outper, String notmarkPer,String total,String branchID) {
        this.location = location;
        this.in = in;
        this.out = out;
        this.notmark = notmark;
        this.date = date;
        this.inPer = inPer;
        this.outper = outper;
        this.notmarkPer = notmarkPer;
        this.totalEmp=total;
        this.branchID=branchID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getNotmark() {
        return notmark;
    }

    public void setNotmark(String notmark) {
        this.notmark = notmark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInPer() {
        return inPer;
    }

    public void setInPer(String inPer) {
        this.inPer = inPer;
    }

    public String getOutper() {
        return outper;
    }

    public void setOutper(String outper) {
        this.outper = outper;
    }

    public String getNotmarkPer() {
        return notmarkPer;
    }

    public void setNotmarkPer(String notmarkPer) {
        this.notmarkPer = notmarkPer;
    }

    public String getTotalEmp() {
        return totalEmp;
    }

    public void setTotalEmp(String totalEmp) {
        this.totalEmp = totalEmp;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }
}
