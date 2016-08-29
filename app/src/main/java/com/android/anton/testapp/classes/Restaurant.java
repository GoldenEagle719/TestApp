package com.android.anton.testapp.classes;

/**
 * Created by Administrator on 8/27/2016.
 */
public class Restaurant {

    private String name;
    private String distance;
    private float rating;
    private String img_url;

    public Restaurant(String name, String distance, float rating, String img_url) {
        this.name = name;
        this.distance = distance;
        this.rating = rating;
        this.img_url = img_url;
    }

    public String getName() {
        return this.name;
    }

    public String getDistance() {
        return this.distance;
    }

    public float getRating() {
        return this.rating;
    }

    public String getImgURL() {
        return this.img_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setImgURL(String img_url) {
        this.img_url = img_url;
    }

}
