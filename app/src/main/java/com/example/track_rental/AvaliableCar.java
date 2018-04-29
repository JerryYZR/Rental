package com.example.track_rental;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 叶泽锐 on 2018/4/23.
 */

public class AvaliableCar extends BmobObject{
    private String storeId;
    private String carId;
    private double price;
    private BmobDate originTime;
    private BmobDate endTime;
    private String city;

    public AvaliableCar(String storeId, String carId,String carName, double price,
                        BmobDate originTime, BmobDate endTime, String city){
        this.storeId = storeId;
        this.carId = carId;
        this.price = price;
        this.originTime = originTime;
        this.endTime = endTime;
        this.city = city;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getCarId() {
        return carId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public BmobDate getEndTime() {
        return endTime;
    }

    public BmobDate getOriginTime() {
        return originTime;
    }

    public void setEndTime(BmobDate endTime) {
        this.endTime = endTime;
    }

    public void setOriginTime(BmobDate originTime) {
        this.originTime = originTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
