package com.genius.payhrms.activity.utility;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Robert
 */

 public  interface AttendanceService {
    /*@Multipart
    @POST("/upload_multi_files/MultiUpload.php")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);*/
    @Multipart
    @POST("post_SelfAttendanceWithImage")
    Call<UploadObject> uploadSingleFile(@Part MultipartBody.Part file, @Part("AEMEmployeeID") String AEMEmployeeID, @Part("Address") String Address, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("SecurityCode") String SecurityCode);

    @Multipart
    @POST("post_DailyAactivityLogWithoutImage")
    Call<UploadObject> dailyactivity(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("ProjectAID") String ProjectAID, @Part("SubProjectAID") String SubProjectAID, @Part("ApprovalStatus") String ApprovalStatus, @Part("Remarks") String Remarks, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("Address") String Address, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode);

    //file
    //AEMEmployeeID
    //ApprovalStatus
    //Remarks
    //Longitude
    //Latitude
    //Address
    //Year
    //Month
    //SecurityCode
    //FName

    @Multipart
    @POST("post_GeofenceMultiEndPointConfiguration")
    Call<UploadObject> createGeoFence(@Part("Geofence") String GeoFence, @Part("LocationName") String LocationName, @Part("GeoFenceId") String GeoFenceId, @Part("SecurityCode") String SecurityCode);

    @Multipart
    @POST("ChangePassword")
    Call<UploadObject> changePassword(@Part("EmployeeId") String EmployeeId, @Part("NewPassword") String NewPassword,@Part("ExistingPassword") String ExistingPassword, @Part("SecurityCode") String SecurityCode);


    @Multipart
    @POST("post_EmployeeDailyActivity")
    Call<UploadObject> dailyactivityForFront(@Part MultipartBody.Part file, @Part("AEMEmployeeID") String AEMEmployeeID,@Part("ApprovalStatus") String ApprovalStatus, @Part("Remarks") String Remarks,@Part("Longitude") String Longitude,@Part("Latitude") String Latitude,@Part("Address") String Address,@Part("Year") String Year,@Part("Month") String Month,@Part("SecurityCode") String SecurityCode,@Part("FName") String FName);

    @Multipart
    @POST("Post_Dailylog_Dayco")
    Call<UploadObject> dailyActivtyDayco(@Part MultipartBody.Part file, @Part("AEMEmployeeID") String AEMEmployeeID, @Part("ApprovalStatus") String ApprovalStatus, @Part("Remarks") String Remarks, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("Address") String Address, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode,@Part("WorkMode") String WorkMode,@Part("Client") String Client, @Part("FName") String FName);



}
