package com.genius.payhrms.activity.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kiit on 05-01-2018.
 */

public class Pref {
    private SharedPreferences _pref;
    private static final String PREF_FILE = "com.deus";
    private SharedPreferences.Editor _editorPref;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    @SuppressLint("CommitPrefEdits")
    public Pref(Context context) {
        _pref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        _editorPref = _pref.edit();
    }


    public void saveFlag(String flag){
        _editorPref.putString("flag", flag);
        _editorPref.commit();
    }

    public String getFlag(){
        return _pref.getString("flag","");
    }
    public void savePoint(String point){
        _editorPref.putString("point", point);
        _editorPref.commit();
    }

    public String getPoint(){
        return _pref.getString("point","");
    }

    public void saveEmpName(String name){
        _editorPref.putString("name", name);
        _editorPref.commit();
    }

    public String getEmpName(){
        return _pref.getString("name","");
    }

       public void saveCtime(String ctime){
        _editorPref.putString("ctime", ctime);
        _editorPref.commit();
    }

    public String getCtime(){
        return _pref.getString("ctime","");
    }

    public void saveloginTime(String ltime){
        _editorPref.putString("ltime", ltime);
        _editorPref.commit();
    }

    public String getloginTime(){
        return _pref.getString("ltime","");
    }


    public void saveEmpId(String empid){
        _editorPref.putString("empid", empid);
        _editorPref.commit();
    }

    public String getEmpId(){
        return _pref.getString("empid","");
    }

    public void saveMenu(String menu){
        _editorPref.putString("menu", menu);
        _editorPref.commit();
    }

    public String getMenu(){
        return _pref.getString("menu","");
    }

    public void saveEmpConId(String emconid){
        _editorPref.putString("emconid", emconid);
        _editorPref.commit();
    }

    public String getEmpConId(){
        return _pref.getString("emconid","");
    }


    public void saveEmpClintId(String emcclid){
        _editorPref.putString("emcclid", emcclid);
        _editorPref.commit();
    }

    public String getEmpClintId(){
        return _pref.getString("emcclid","");
    }

    public void saveEmpClintOffId(String emccloid){
        _editorPref.putString("emccloid", emccloid);
        _editorPref.commit();
    }

    public String getEmpClintOffId(){
        return _pref.getString("emccloid","");
    }

    public void saveMasterId(String Master){
        _editorPref.putString("Master", Master);
        _editorPref.commit();
    }

    public String getMasterId(){
        return _pref.getString("Master","");
    }

    public void saveUserType(String UserType){
        _editorPref.putString("UserType", UserType);
        _editorPref.commit();
    }

    public String getUserType(){
        return _pref.getString("UserType","");
    }


    public void saveCTCURL(String CTCURL){
        _editorPref.putString("CTCURL", CTCURL);
        _editorPref.commit();
    }

    public String getCTCURL(){
        return _pref.getString("CTCURL","");
    }


    public void saveManageId(String ManageId){
        _editorPref.putString("ManageId", ManageId);
        _editorPref.commit();
    }

    public String getManageId(){
        return _pref.getString("Weeklyoff","");
    }

    public void saveWeeklyoff(String Weeklyoff){
        _editorPref.putString("Weeklyoff", Weeklyoff);
        _editorPref.commit();
    }

    public String getWeeklyoff(){
        return _pref.getString("Weeklyoff","");
    }

    public void saveOnLeave(String OnLeave){
        _editorPref.putString("OnLeave", OnLeave);
        _editorPref.commit();
    }

    public String getOnLeave(){
        return _pref.getString("OnLeave","");
    }

    public void saveLeaveUrl(String LeaveUrl){
        _editorPref.putString("LeaveUrl", LeaveUrl);
        _editorPref.commit();
    }

    public String getLeaveUrl(){
        return _pref.getString("LeaveUrl","");
    }


    public void saveSecurityCode(String SecurityCode){
        _editorPref.putString("SecurityCode", SecurityCode);
        _editorPref.commit();
    }

    public String getSecurityCode(){
        return _pref.getString("SecurityCode","");
    }


    public void saveBackAttd(String BackAttd){
        _editorPref.putString("BackAttd", BackAttd);
        _editorPref.commit();
    }

    public String getBackAttd(){
        return _pref.getString("BackAttd","");
    }

    public void saveAttdImg(String AttdImg){
        _editorPref.putString("AttdImg", AttdImg);
        _editorPref.commit();
    }

    public String getAttdImg(){
        return _pref.getString("AttdImg","");
    }


    public void saveSup(String Sup){
        _editorPref.putString("Sup", Sup);
        _editorPref.commit();
    }

    public String getSup(){
        return _pref.getString("Sup","");
    }

    public void saveAtteType(String AtteType){
        _editorPref.putString("AtteType", AtteType);
        _editorPref.commit();
    }

    public String getAtteType(){
        return _pref.getString("AtteType","");
    }


    public void saveFlagLocation(String FlagLocation){
        _editorPref.putString("FlagLocation", FlagLocation);
        _editorPref.commit();
    }

    public String getFlagLocation(){
        return _pref.getString("FlagLocation","");
    }


    public void savePassword(String Password){
        _editorPref.putString("Password", Password);
        _editorPref.commit();
    }

    public String getPassword(){
        return _pref.getString("Password","");
    }

    public void saveLoginID(String LoginID){
        _editorPref.putString("LoginID", LoginID);
        _editorPref.commit();
    }

    public String getLoginID(){
        return _pref.getString("LoginID","");
    }


    public void saveCheckFlag(String ckflag){
        _editorPref.putString("ckflag", ckflag);
        _editorPref.commit();
    }

    public String getCheckFlag(){
        return _pref.getString("ckflag","");
    }


    public void savePayrollFlag(String PayrollFlag){
        _editorPref.putString("PayrollFlag", PayrollFlag);
        _editorPref.commit();
    }

    public String getPayrollFlag(){
        return _pref.getString("PayrollFlag","");
    }

    public void saveImgFlag(String ImgFlag){
        _editorPref.putString("ImgFlag", ImgFlag);
        _editorPref.commit();
    }

    public String getImgFlag(){
        return _pref.getString("ImgFlag","");
    }


    public void saveDailyActivityFlag(String DailyActivityFlag){
        _editorPref.putString("DailyActivityFlag", DailyActivityFlag);
        _editorPref.commit();
    }

    public String getDailyActivityFlag(){
        return _pref.getString("DailyActivityFlag","");
    }

    public void saveAttenFlag(String AttenFlag){
        _editorPref.putString("AttenFlag", AttenFlag);
        _editorPref.commit();
    }

    public String getAttenFlag(){
        return _pref.getString("AttenFlag","");
    }

    public void saveDailyLogFlag(String DailyLogFlag){
        _editorPref.putString("DailyLogFlag", DailyLogFlag);
        _editorPref.commit();
    }

    public String getDailyLogFlag(){
        return _pref.getString("DailyLogFlag","");
    }

    public void saveOffAttnFlag(String OffAttnFlag){
        _editorPref.putString("OffAttnFlag", OffAttnFlag);
        _editorPref.commit();
    }

    public String getOffAttnFlag(){
        return _pref.getString("OffAttnFlag","");
    }



    public void saveAddress(String Address){
        _editorPref.putString("Address", Address);
        _editorPref.commit();
    }

    public String getAddress(){
        return _pref.getString("Address","");
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        _editorPref.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        _editorPref.commit();
    }

    public boolean isFirstTimeLaunch() {
        return _pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void saveOwnLong(String OwnLong){
        _editorPref.putString("OwnLong", OwnLong);
        _editorPref.commit();
    }

    public String getOwnLong(){
        return _pref.getString("OwnLong","");
    }

    public void saveOwnLat(String OwnLat){
        _editorPref.putString("OwnLat", OwnLat);
        _editorPref.commit();
    }

    public String getOwnLat(){
        return _pref.getString("OwnLat","");
    }

    public void saveEndLat(String EndLat){
        _editorPref.putString("EndLat", EndLat);
        _editorPref.commit();
    }

    public String getEndLat(){
        return _pref.getString("EndLat","");
    }


    public void saveEndLong(String EndLong){
        _editorPref.putString("EndLong", EndLong);
        _editorPref.commit();
    }

    public String getEndLong(){
        return _pref.getString("EndLong","");
    }




    public void saveDemoFlag(String DemoFlag){
        _editorPref.putString("DemoFlag", DemoFlag);
        _editorPref.commit();
    }

    public String getDemoFlag(){
        return _pref.getString("DemoFlag","");
    }



    public void saveEndPoint(String EndPoint){
        _editorPref.putString("EndPoint", EndPoint);
        _editorPref.commit();
    }

    public String getEndPoint(){
        return _pref.getString("EndPoint","");
    }

    public void saveFenceToast(String FenceToast){
        _editorPref.putString("FenceToast", FenceToast);
        _editorPref.commit();
    }

    public String getFenceToast(){
        return _pref.getString("FenceToast","");
    }





    public void saveTutorialText(String TutorialText){
        _editorPref.putString("TutorialText", TutorialText);
        _editorPref.commit();
    }

    public String getTutorialText(){
        return _pref.getString("TutorialText","");
    }



    public void saveGeoFenceConfig(String GeoFenceConfig){
        _editorPref.putString("GeoFenceConfig", GeoFenceConfig);
        _editorPref.commit();
    }

    public String getGeoFenceConfig(){
        return _pref.getString("GeoFenceConfig","");
    }




    public void saveGeoFenceMenuFlag(String GeoFenceMenuFlag){
        _editorPref.putString("GeoFenceMenuFlag", GeoFenceMenuFlag);
        _editorPref.commit();
    }

    public String getGeoFenceMenuFlag(){
        return _pref.getString("GeoFenceMenuFlag","");
    }



    public void saveGeoFenceFlag(String GeoFenceFlag){
        _editorPref.putString("GeoFenceFlag", GeoFenceFlag);
        _editorPref.commit();
    }

    public String getGeoFenceFlag(){
        return _pref.getString("GeoFenceFlag","");
    }



    public void saveTutorialFlag(String TutorialFlag){
        _editorPref.putString("TutorialFlag", TutorialFlag);
        _editorPref.commit();
    }

    public String getTutorialFlag(){
        return _pref.getString("TutorialFlag","");
    }



    public void saveFenceSubMenu(String FenceSubMenu){
        _editorPref.putString("FenceSubMenu", FenceSubMenu);
        _editorPref.commit();
    }

    public String getFenceSubMenu(){
        return _pref.getString("FenceSubMenu","");
    }



    public void saveInFlag(String InFlag){
        _editorPref.putString("InFlag", InFlag);
        _editorPref.commit();
    }

    public String getInFlag(){
        return _pref.getString("InFlag","");
    }


    public void saveOutFlag(String OutFlag){
        _editorPref.putString("OutFlag", OutFlag);
        _editorPref.commit();
    }

    public String getOutFlag(){
        return _pref.getString("OutFlag","");
    }

    public void saveMulOutFlag(String MulOutFlag){
        _editorPref.putString("MulOutFlag", MulOutFlag);
        _editorPref.commit();
    }

    public String getMulOutFlag(){
        return _pref.getString("MulOutFlag","");
    }


    public void saveMulInFlag(String InFlag){
        _editorPref.putString("InFlag", InFlag);
        _editorPref.commit();
    }

    public String getMulFlag(){
        return _pref.getString("InFlag","");
    }


    public void saveDate(String Date){
        _editorPref.putString("Date", Date);
        _editorPref.commit();
    }

    public String getDate(){
        return _pref.getString("Date","");
    }




    public void saveFenceId(String FenceId){
        _editorPref.putString("FenceId", FenceId);
        _editorPref.commit();
    }

    public String getFenceId(){
        return _pref.getString("FenceId","");
    }



    public void saveUpdateFlag(String UpdateFlag){
        _editorPref.putString("UpdateFlag", UpdateFlag);
        _editorPref.commit();
    }

    public String getUpdateFlag(){
        return _pref.getString("UpdateFlag","");
    }


    public void saveLanguage(String Language){
        _editorPref.putString("Language", Language);
        _editorPref.commit();
    }

    public String getLanguage(){
        return _pref.getString("Language","");
    }


    public void saveLoginFlag(String LoginFlag){
        _editorPref.putString("LoginFlag", LoginFlag);
        _editorPref.commit();
    }

    public String getLoginFlag(){
        return _pref.getString("LoginFlag","");
    }


    public void saveRefreshToken(String RefreshToken){
        _editorPref.putString("RefreshToken", RefreshToken);
        _editorPref.commit();
    }

    public String getRefreshToken(){
        return _pref.getString("RefreshToken","");
    }


    public void saveLivetrackingServiceFlag(String LivetrackingServiceFlag){
        _editorPref.putString("LivetrackingServiceFlag", LivetrackingServiceFlag);
        _editorPref.commit();
    }

    public String getLivetrackingServiceFlag(){
        return _pref.getString("LivetrackingServiceFlag","");
    }


    public void saveLivetrackingFlag(String LivetrackingFlag){
        _editorPref.putString("LivetrackingFlag", LivetrackingFlag);
        _editorPref.commit();
    }

    public String getLivetrackingFlag(){
        return _pref.getString("LivetrackingFlag","");
    }


    public void saveLanguageFlag(String LanguageFlag){
        _editorPref.putString("LanguageFlag", LanguageFlag);
        _editorPref.commit();
    }

    public String getLanguageFlag(){
        return _pref.getString("LanguageFlag","");
    }

    public void saveAccessFlag(String AccessFlag){
        _editorPref.putString("AccessFlag", AccessFlag);
        _editorPref.commit();
    }

    public String getAccessFlag(){
        return _pref.getString("AccessFlag","");
    }


    public void saveITView(String ITView){
        _editorPref.putString("ITView", ITView);
        _editorPref.commit();
    }

    public String getITView(){
        return _pref.getString("ITView","");
    }

    public void saveTeamReportFlag(String TeamReportFlag){
        _editorPref.putString("TeamReportFlag", TeamReportFlag);
        _editorPref.commit();
    }

    public String getTeamReportFlag(){
        return _pref.getString("TeamReportFlag","");
    }

    public void saveIsModifiedFlag(String IsModifiedFlag){
        _editorPref.putString("IsModifiedFlag", IsModifiedFlag);
        _editorPref.commit();
    }

    public String getIsModifiedFlag(){
        return _pref.getString("IsModifiedFlag","");
    }



    public void saveIpAddress(String IpAddress){
        _editorPref.putString("IpAddress", IpAddress);
        _editorPref.commit();
    }

    public String getIpAddress(){
        return _pref.getString("IpAddress","");
    }


    public void saveEmpMapAccessFlag(String EmpMapAccessFlag){
        _editorPref.putString("EmpMapAccessFlag", EmpMapAccessFlag);
        _editorPref.commit();
    }

    public String getEmpMapAccessFlag(){
        return _pref.getString("EmpMapAccessFlag","");
    }




    public void saveWeeklyOffFlag(String WeeklyOffFlag){
        _editorPref.putString("WeeklyOffFlag", WeeklyOffFlag);
        _editorPref.commit();
    }

    public String getWeeklyOffFlag(){
        return _pref.getString("WeeklyOffFlag","");
    }


    public void saveHolidayMapFlag(String HolidayMapFlag){
        _editorPref.putString("HolidayMapFlag", HolidayMapFlag);
        _editorPref.commit();
    }

    public String getHolidayMapFlag(){
        return _pref.getString("HolidayMapFlag","");
    }



    public void saveMsg(String Msg){
        _editorPref.putString("Msg", Msg);
        _editorPref.commit();
    }

    public String getMsg(){
        return _pref.getString("Msg","");
    }

    public void saveMsgStatus(boolean MsgStatus){
        _editorPref.putBoolean("MsgStatus", MsgStatus);
        _editorPref.commit();
    }

    public boolean getMsgStatus(){

        return _pref.getBoolean("MsgStatus",false);
    }
    public void saveempCode(String empcode){
        _editorPref.putString("empcode", empcode);
        _editorPref.commit();
    }

    public String getempcode(){
        return _pref.getString("empcode","");
    }
    public void saveempName(String empname){
        _editorPref.putString("empname", empname);
        _editorPref.commit();
    }

    public String getempname(){
        return _pref.getString("empname","");
    }

    public void saveHRManualID(int HRManualID){
        _editorPref.putInt("HRManualID", HRManualID);
        _editorPref.commit();
    }

    public int getHRManualID(){
        return _pref.getInt("HRManualID",0);
    }


    public void savePunchFromID(String PunchFromID){
        _editorPref.putString("PunchFromID", PunchFromID);
        _editorPref.commit();
    }

    public String getPunchFromID(){
        return _pref.getString("PunchFromID","");
    }


    public void saveAccessToken(String AccessToken){
        _editorPref.putString("AccessToken", AccessToken);
        _editorPref.commit();
    }

    public String getAccessToken(){
        return _pref.getString("AccessToken","");
    }



    public void saveShiftFlag(String ShiftFlag){
        _editorPref.putString("ShiftFlag", ShiftFlag);
        _editorPref.commit();
    }

    public String getShiftFlag(){
        return _pref.getString("ShiftFlag","");
    }







}

