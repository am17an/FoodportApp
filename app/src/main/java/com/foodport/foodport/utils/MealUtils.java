package com.foodport.foodport.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by amangupta on 02/06/15.
 */
public class MealUtils {

    private static String APPNAME = "foodport";
    private static String MEALS = "meals";

    public static void clearMeals(Activity activity){
        SharedPreferences settings = activity.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();

        if(settings.contains(MEALS)){
            editor.remove(MEALS);
            editor.commit();
        }
    }

    public static void getMeals(Activity activity) {

    }
}
