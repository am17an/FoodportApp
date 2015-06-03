package com.foodport.foodport.service;

import com.foodport.foodport.model.Token;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by amangupta on 03/06/15.
 */
public interface DetailApi  {

    @POST("/api/detail/register-token")
    void registerDevice(@Body Token token, Callback<String> response);
}
