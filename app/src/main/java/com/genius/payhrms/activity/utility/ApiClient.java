package com.genius.payhrms.activity.utility;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;



public class ApiClient {
    public  static  String baseUrl="";
    public static GetDataWorkingDay getDataWorkingDay = null;

    public static GetDataWorkingDay getService()
    {
        if(getDataWorkingDay==null)
        {
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            getDataWorkingDay=retrofit.create(GetDataWorkingDay.class);
        }
        return getDataWorkingDay;
    }
    public interface GetDataWorkingDay {

        @Multipart
        @POST("gcl_post_attedance")
            //Call<AttendanceManageModule> getDatas(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("Address") String Address, @Part("Longitude") String Longitude, @Part("Latitude")String Latitude, @Part("SecurityCode") String SecurityCode, @Part("Image1")String Image1 );
        retrofit2.Call<UploadObject> getDatas(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("Address") String Address, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("SecurityCode") String SecurityCode);
        @Multipart
        @POST("gcl_post_OfflineAttedance")
        Call<UploadObject> offlineAttn(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("Address") String Address, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("SecurityCode")String SecurityCode, @Part("Attendance")String Attendance);

    }



   
}
