package com.foodport.foodport.utils;

import retrofit.RestAdapter;

/**
 * Created by amangupta on 03/06/15.
 */
public class ApiAdapter {

    static RestAdapter mRestAdapter;
    static final String endpointUrl = "http://192.168.1.110:9000"; // Development



    private static void initialize() {
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(endpointUrl)
                .build();
    }

    public static RestAdapter getInstance() {
        if(mRestAdapter == null) {
            initialize();

        }
        return mRestAdapter;
    }


}
