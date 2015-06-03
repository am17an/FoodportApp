package com.foodport.foodport.service;

import com.foodport.foodport.model.Meal;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by amangupta on 31/05/15.
 */
public interface MealService {
    @GET("/api/meals")
    Observable<List<Meal>> listMeals();
}
