package com.example.track_rental;

import android.app.Application;

/**
 * Created by 叶泽锐 on 2018/1/2.
 */

public class DataApplication extends Application {

    //声明一个变量
    public String City = "";

    public String City2 = "";

    public String Id = "15967186973";

    public String Store1 = "";

    public String Store2 = "";

    public String Origin = "";

    public String Destination = "";

    public boolean Choose = true;

    public Double Lat = 0.00;
    public Double Lon = 0.00;

    public Double Lat1 = 30.2221000000;
    public Double Lon1 = 120.1283300000;

    public Double getLat1() {
        return Lat1;
    }

    public Double getLon1() {
        return Lon1;
    }

    public void setLon1(Double lon1) {
        Lon1 = lon1;
    }

    public void setLat1(Double lat1) {
        Lat1 = lat1;
    }

    public void setChoose(boolean choose) {
        Choose = choose;
    }

    public boolean getChoose() {
        return Choose;
    }

    public String getStore1() {
        return Store1;
    }

    public String getStore2() {
        return Store2;
    }

    public void setStore1(String store1) {
        Store1 = store1;
    }

    public void setStore2(String store2) {
        Store2 = store2;
    }

    public String getCity2() {
        return City2;
    }

    public void setCity2(String city2) {
        City2 = city2;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCity() {
        return City;
    }

    public String getDestination() {
        return Destination;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public Double getLat() {
        return Lat;
    }

    public Double getLon() {
        return Lon;
    }

    public void setLon(Double lon) {
        Lon = lon;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public String getId() {
        return Id;
    }
}
