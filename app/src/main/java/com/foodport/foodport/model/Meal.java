package com.foodport.foodport.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amangupta on 31/05/15.
 */

public class Meal {
    @SerializedName("name")
    private String name;

    @SerializedName("desc")
    private String description;

    @SerializedName("_id")
    private String id;

    @SerializedName("price")
    private String price;

    @SerializedName("quantityLeft")
    private int quantityLeft;

    @SerializedName("images")
    private List<MealImage> mealImages;

    @SerializedName("veg")
    private Boolean veg;


    public int getQuantityLeft() {
        return quantityLeft;
    }

    public Boolean getVeg() {
        if(veg == null) {
            return false;
        }
        return true;
    }

    public List<MealImage> getMealImages() {
        return mealImages;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {  return name; }

    public String getId() {
        return id;
    }
}
