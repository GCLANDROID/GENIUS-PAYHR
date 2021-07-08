package com.genius.hrms.activity.utility;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
   @Multipart
   @POST("post_DailyLogTatGY")
   Call<UploadObject> dailyactivityTATAGY(@Part MultipartBody.Part file, @Part("AEMEmployeeID") String AEMEmployeeID, @Part("ApprovalStatus") String ApprovalStatus, @Part("Remarks") String Remarks, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("Address") String Address, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part("FName") String FName);


    @Multipart
    @POST("post_GeofenceMultiEndPointConfiguration")
    Call<UploadObject> createGeoFence(@Part("Geofence") String GeoFence, @Part("LocationName") String LocationName, @Part("GeoFenceId") String GeoFenceId, @Part("SecurityCode") String SecurityCode);

    @Multipart
    @POST("ChangePassword")
    Call<UploadObject> changePassword(@Part("EmployeeId") String EmployeeId, @Part("NewPassword") String NewPassword, @Part("SecurityCode") String SecurityCode);


    @Multipart
    @POST("post_EmployeeDailyActivity")
    Call<UploadObject> dailyactivityForFront(@Part MultipartBody.Part file, @Part("AEMEmployeeID") String AEMEmployeeID,@Part("ApprovalStatus") String ApprovalStatus, @Part("Remarks") String Remarks,@Part("Longitude") String Longitude,@Part("Latitude") String Latitude,@Part("Address") String Address,@Part("Year") String Year,@Part("Month") String Month,@Part("SecurityCode") String SecurityCode,@Part("FName") String FName);



}
