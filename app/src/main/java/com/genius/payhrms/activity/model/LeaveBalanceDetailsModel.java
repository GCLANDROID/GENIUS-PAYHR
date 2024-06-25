package com.genius.payhrms.activity.model;

public class LeaveBalanceDetailsModel {
    String leave,leaveBalance,leaveTaken;

    public LeaveBalanceDetailsModel(String leave, String leaveBalance, String leaveTaken) {
        this.leave = leave;
        this.leaveBalance = leaveBalance;
        this.leaveTaken = leaveTaken;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(String leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public String getLeaveTaken() {
        return leaveTaken;
    }

    public void setLeaveTaken(String leaveTaken) {
        this.leaveTaken = leaveTaken;
    }
}
