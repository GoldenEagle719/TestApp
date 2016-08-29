package com.android.anton.testapp.classes;

/**
 * Created by Administrator on 8/29/2016.
 */
public class LocationForCity {
    private String name;
    private double lat;
    private double lng;

    public LocationForCity(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

}
