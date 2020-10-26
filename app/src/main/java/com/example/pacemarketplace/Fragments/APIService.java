package com.example.pacemarketplace.Fragments;

import com.example.pacemarketplace.Notifications.MyResponse;
import com.example.pacemarketplace.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=ADD YOUR KEY FROM FIREBASE PROJECT HERE"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
