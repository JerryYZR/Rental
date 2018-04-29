package com.example.track_rental;

import cn.bmob.v3.BmobObject;

/**
 * Created by 叶泽锐 on 2018/4/23.
 */

public class CarTable extends BmobObject{

    private String modelNumber;

    private String carType;

    private String carId;

    public CarTable(String modelNumber, String carType, String carId){
        this.carId = carId;
        this.carType = carType;
        this.modelNumber = modelNumber;
    }

    public String getCarId() {
        return carId;
    }

    public String getCarType() {
        return carType;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }
}
