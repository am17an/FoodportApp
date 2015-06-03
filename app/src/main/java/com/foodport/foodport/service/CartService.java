package com.foodport.foodport.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.foodport.foodport.model.Meal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amangupta on 01/06/15.
 * Singleton
 */
public class CartService {

    private static String APPNAME="foodport";
    private static String CART = "cart";
    private static String CLASS = CartService.CLASS;

    private static HashMap<String, Integer> cart;
    private static List<Meal> meals;
    private static CartService instance;
    private static Activity context;

    public static CartService getInstance(Activity activity) {
        if(instance == null) {
            Log.v("Cart Service", "Cart Service being created");
            instance = new CartService();
            context = activity;

            cart = getInitialCart();


        }
        return instance;
    }

    private CartService(){

    }

    private static HashMap<String, Integer> getInitialCart() {
        SharedPreferences settings = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);

        cart = new HashMap<String, Integer>();
        if(settings.contains(CART)){
            // INVARIANT: Cart will be always have non-stale data. Whenever we receive a push for
            // new menu, we clear SharedPreferences.
            Log.v(CLASS, "Loading Cart from Shared Pref");
            String jsonMeals = settings.getString(CART, null);
            Gson gson = new Gson();
            Log.v("CART", jsonMeals);
            cart = gson.fromJson(jsonMeals, new TypeToken<HashMap<String, Integer>>(){}.getType());

        }

        return cart;
    }

    public static void clearCart() {

        Log.v(CLASS, "Cart being cleared");

        SharedPreferences settings = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();

        if(settings.contains(CART)) {
            editor.remove(CART);
            editor.commit();
        }

    }

    private static void printCart() {
        for (HashMap.Entry<String, Integer> entry : cart.entrySet()) {
            String meal = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("key, " + meal + " value " + value );
        }
    }

    public void saveState() {
        SharedPreferences settings = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        String jsonMeals = gson.toJson(cart);

        editor.putString(CART, jsonMeals);
        editor.commit();
    }


    public void addMeal(Meal meal) {
        Log.v("Inside Cart Service", meal.getId());
        if(cart.containsKey(meal.getId())){
            int quantity = cart.get(meal.getId());
            cart.put(meal.getId(), quantity + 1);
        } else {
            cart.put(meal.getId(), 1);
        }
    }

    public void removeMeal(Meal meal) {
        if(cart.containsKey(meal.getId())) {
            int quantity = cart.get(meal.getId());
            if(quantity - 1 == 0) {
                cart.remove(meal.getId());
            } else {
                cart.put(meal.getId(), quantity - 1);
            }
        }
    }

    public HashMap<String, Integer> getCart() {
        return cart;
    }

    public int getMealQuantity(Meal meal) {
        if(cart.get(meal.getId()) == null) {
            return 0;
        } else {
            return cart.get(meal.getId());
        }
    }

    public int getTotalItems() {
       Collection<Integer> values = cart.values();
       int total = 0;

        for (Integer value : values) {
            total += value;
        }
        return total;
    }
}
