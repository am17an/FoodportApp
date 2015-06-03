package com.foodport.foodport.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amangupta on 03/06/15.
 */
public class Token {

    @SerializedName("token")
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
