package com.genius.hrms.activity.chat.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAfDkwYPg:APA91bFUxAKlOhagWuUvfXciNfmYMAo7wblneNkBHvdYIGxr_GTVY4IOvGDoAWlLLxl5Labgt0KD83KrWZqhHTkbCRHyjWxn2g-nFYMBgr7cnyU3D6mjPq4OjyL-8DEWqMtTHIC0Sxpl"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
