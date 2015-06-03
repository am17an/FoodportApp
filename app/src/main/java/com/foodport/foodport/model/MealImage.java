package com.foodport.foodport.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amangupta on 31/05/15.
 */
public class MealImage {
    @SerializedName("url")
    private String url;

    @SerializedName("height")
    private String height;

    @SerializedName("width")
    private String width;

    public String getUrl() {
        return url;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }
}
