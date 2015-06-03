package com.foodport.foodport.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.foodport.foodport.MainActivity;
import com.foodport.foodport.MealAdapter;
import com.foodport.foodport.model.Meal;
import com.foodport.foodport.utils.ApiAdapter;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.List;

import retrofit.RestAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amangupta on 02/06/15.
 */
public class MyGcmListenerService extends GcmListenerService {

    static final String TAG = "MyGCMListenerService";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;


    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("type");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if(message == "Reload"){
            RestAdapter restAdapter = ApiAdapter.getInstance();
            MealService mealService = restAdapter.create(MealService.class);


            mealService.listMeals()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Meal>>() {
                        @Override
                        public void call(List<Meal> meals) {

                            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                                    .getSystemService(Context.ACTIVITY_SERVICE);

                            List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);

                            boolean isActivityFound = false;

                            if(services.get(0).topActivity.getPackageName().toString()
                                    .equalsIgnoreCase(getApplicationContext().getPackageName().toString())) {
                                isActivityFound = true;
                            }

                            if(isActivityFound) {
                                
                            }

                        }
                    });
        }

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.support.v7.appcompat.R.drawable.notification_template_icon_bg)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
