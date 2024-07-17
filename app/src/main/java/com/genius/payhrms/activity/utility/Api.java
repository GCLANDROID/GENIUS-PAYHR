package com.genius.payhrms.activity.utility;

public class Api {

    public static String baseurl = "https://cloud.geniusconsultant.com/GHRMSApi_v2/api/";
    //public static String baseurl="http://171.16.1.136/GHRMSApi_V2/api/"; //todo: SUMAN DA
    //public static String baseurl="http://171.16.2.67/GHRMSApi_V2_DevMode/api/";
    //public static String baseurl_SubhamDa="http://171.16.2.67/GHRMSApi_V2_DevMode/api/";
    public static String sLoginapi = baseurl + "Login/LoginUser";
    public static String sProfileapi = baseurl + "Profile/GetProfileDetails";
    public static String sWesternProfileapi = baseurl + "Profile/GetWesternProfileDetails";
    public static String sProfileImageapi = baseurl + "Profile/ProfilePic";
    public static String sHolidayapi = baseurl + "Holiday/GCLHolidayList_New";
    public static String sCalendarapi = baseurl + "Attendance/GetEmployeeAttendanceReport";
    public static String sMenuapi = baseurl + "MenuAccess/MenuItemList";
    public static String sChangePasswordapi = baseurl + "Login/ChangePassword";
    public static String sElearningapi = baseurl + "ELearning/GetManualList";
    public static String sForgotPasswordapi = baseurl + "Profile/GClForgotPassword";
    // public static String sversioncheckapi=baseurl+"VersionCheck/ApkVersionAndAutoUpdateStatus";
    public static String sapprovercheckapi = baseurl + "Attendance/LeaveApplicationApprover";
    public static String sselfattendanceapi = baseurl + "Attendance/PostSelfAttendance";
    public static String sselfattendanceimageapi = baseurl + "FileUpload/PostSelfAttendanceWithImage";
    public static String sAttendanceRegularizationapi = baseurl + "Attendance/AttendanceBakLog";
    public static String sAttendanceRegularizationsaveapi = baseurl + "DailyLog/AttendanceBackLogSave";
    public static String sAttendanceReportapi = baseurl + "DailyLog/GetEmployeeAttendanceReport";
    public static String sGetEmployeeAttendanceReport = baseurl + "Attendance/GetEmployeeAttendanceReport";
    public static String sFileUploadPostDailyLogTatGY = baseurl + "FileUpload/PostDailyLogTatGY";
    public static String sPostDailyLogWithoutImage = baseurl + "DailyLog/PostDailyLogWithoutImage";

    public static String sGetOfflineDailyLogActivity = baseurl + "DailyLog/GetOfflineDailyLogActivity"; // New imp
    public static String sGetHolidayListCheck = baseurl + "DailyLog/GetHolidayListCheck"; // New imp
    //public static String sPostDailyLogTatGY= baseurl+"FileUpload/PostDailyLogTatGY"; // New imp
    public static String sPostOfflineDailyLogActivity = baseurl + "DailyLog/PostOfflineDailyLogActivity"; // New imp
    public static String sPostQRAttendance = baseurl + "Attendance/PostQRAttendance"; // New imp
    public static String sGetDailyActivityLogGCL = baseurl + "DailyLog/GetDailyActivityLogGCL"; // New imp

    public static String sSingleAttendanceExistanceStatus = baseurl + "Attendance/SingleAttendanceExistanceStatus"; // New imp

    public static String sPostGeoFenceAndAttendanceArchisman = baseurl + "FileUpload/PostGeoFenceAndAttendanceArchisman"; // New imp
    //Dayco
    public static String sPostAttendanceStatusDayco = baseurl + "FileUpload/PostAttendanceStatusDayco";
    public static String sPostAttendanceDayco = baseurl + "FileUpload/PostAttendanceDayco";
    public static String sGetClientDayco = baseurl + "FileUpload/GetClientDayco";
    public static String sGetOfflineDailyLogActivityDayco = baseurl + "DailyLog/GetOfflineDailyLogActivityDayco";

    //MenuAccess/GetMenuOnOff
    public static String sGetMenuOnOff = baseurl + "MenuAccess/GetMenuOnOff";


    //TODO: LEAVE
    //Leave/CheckLeaveViewSummary
    public static String sCheckLeaveViewSummary = baseurl + "Leave/CheckLeaveViewSummary";
    //Leave/BindViewSummary
    public static String sBindViewSummary = baseurl + "Leave/BindViewSummary";

    public static String sLeaveDayDetailsapi = baseurl + "Leave/DayDetails";
    public static String sLeaveModeapi = baseurl + "Leave/GetLeaveMode";
    public static String sLeaveStartCheckapi = baseurl + "Leave/CheckLeaveStartDayStatus";
    public static String sLeaveDetails = baseurl + "Leave/LeaveApplicationDetails";
    public static String sLeaveAdd = baseurl + "Leave/LeaveAdd";
    //public static String sLeaveAdd=baseurl_SubhamDa+"Leave/LeaveAdd";
    public static String sGetCompOffBreakUp = baseurl + "Leave/GetCompOffBreakUp";
    //public static String sGetCompOffBreakUp = baseurl_SubhamDa+"Leave/GetCompOffBreakUp";

    //Leave/DeleteLeaveApplication
    public static String sDeleteLeaveApplication = baseurl + "Leave/DeleteLeaveApplication";

    public static String sLeaveReportapi = baseurl + "Leave/LeaveApplicationDeatilsForApplicant";
    public static String sSelfAttendanceApprovalPending = baseurl + "Attendance/SelfAttendanceApprovalPending";
    public static String sSelfAttendanceApproval = baseurl + "Attendance/SelfAttendanceApproval";
    public static String sProfilePic = baseurl + "Profile/ProfilePic";

    public static String sPayslipapi = baseurl + "Payroll/GetPaySlip";
    public static String sCommomddlapi = baseurl + "General/GetCommonDDL";

    public static String sCompanyAssetapi = baseurl + "Profile/GetEmployeeAsset";

    public static String sApproverLeaveItemapi = baseurl + "Leave/ApproverLeaveApp";

    public static String sLeaveApprovedapi = baseurl + "Leave/ApprovedApplication";
    public static String sLeaveDeleteByApproverapi = baseurl + "Leave/DeleteLeaveApplicationByApprover";
    public static String UserDeviceDetails = baseurl + "VersionCheck/saveUserdeviceDetails";
    //Attendance/PostQRAttendance
    //public static String sPostQRAttendance=baseurl+"Attendance/PostQRAttendance";

    public static String sversioncheckapi = baseurl + "Login/forcetoUpdate";
    public static String sGetPunchtypeapi = baseurl + "Attendance/GetPunchtype";

    public static String sPostSelfAttendanceShalimarapi = baseurl + "Attendance/PostSelfAttendanceShalimar";
    public static String sSaveODApplicationDetails = baseurl + "IMFALeave/SaveODApplicationDetails";
    public static String sApproverCheckApi = baseurl + "Attendance/LeaveApplicationApprover";
    public static String sGetAdjustmentApplicationAllDetails = baseurl + "IMFALeave/GetAdjustmentApplicationAllDetails";
    public static String sGetAdjustmentDetailsForGrid = baseurl + "IMFALeave/GetAdjustmentDetailsforGrid";
    public static String sDeleteAdjutmentApplication = baseurl + "IMFALeave/DeleteAdjutmentApplication";
    public static String sSaveAdjustmentApprovalRejected = baseurl + "IMFALeave/SaveAdjustmentApprovalRejected";
    public static String sGetAdjutmentApplicationForApprover = baseurl + "IMFALeave/GetAdjutmentApplicationForApprover";
    public static String sDeleteAdjutmentApplicationForApprover = baseurl + "IMFALeave/DeleteAdjutmentApplicationForApprover";
}
