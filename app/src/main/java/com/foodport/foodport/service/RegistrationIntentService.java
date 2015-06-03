package com.foodport.foodport.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.foodport.foodport.model.Token;
import com.foodport.foodport.utils.ApiAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by amangupta on 02/06/15.
 */
public class RegistrationIntentService extends IntentService {
    public static String TAG="RegistrationIntentService";

    private static final String SENDER_ID = "717877892249";

    private static final String SENT_TOKEN_TO_SERVER = "sentToServer";

    public RegistrationIntentService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(
                    SENDER_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null
            );

            Log.v(TAG, token);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("foodport", Context.MODE_PRIVATE);

            if(!sharedPreferences.contains(SENT_TOKEN_TO_SERVER)) {

                sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
            }
            sendRegistrationTokenToServer(token);
        } catch(IOException e) {
            Log.e("Error Occured", e.getMessage());
        }
    }



    public void sendRegistrationTokenToServer(String token) {
        DetailApi service = ApiAdapter.getInstance().create(DetailApi.class);
        Token tokenClass = new Token();
        tokenClass.setToken(token);
        service.registerDevice(tokenClass, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v(TAG, s);
            }

            @Override
            public void failure(RetrofitError error) {
               Log.v(TAG, error.getMessage());
            }
        });
    }
}
