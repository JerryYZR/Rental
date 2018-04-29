package com.example.track_rental;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 叶泽锐 on 2018/4/23.
 */

public class OrderTable extends BmobObject{
    private String clientPhoneNumber;
    private String modelNumber;
    private String carType;
    private String storeId;
    private String storeName;
    private double price;
    private BmobDate originTime;
    private BmobDate endTime;
    private double orderEvaluation;
    private boolean finishOrNot;

    public OrderTable(String storeId, String storeName, double price, String clientPhoneNumber,
                      BmobDate originTime, BmobDate endTime, double orderEvaluation,
                      boolean finishOrNot, String modelNumber, String carType){
        this.carType = carType;
        this.clientPhoneNumber = clientPhoneNumber;
        this.modelNumber = modelNumber;
        this.storeId = storeId;
        this.storeName = storeName;
        this.price = price;
        this.orderEvaluation = orderEvaluation;
        this.originTime = originTime;
        this.endTime = endTime;
        this.finishOrNot = finishOrNot;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getCarType() {
        return carType;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public boolean getFinishOrNot() {
        return finishOrNot;
    }

    public void setFinishOrNot(boolean finishOrNot) {
        this.finishOrNot = finishOrNot;
    }

    public BmobDate getEndTime() {
        return endTime;
    }

    public String getStoreId() {
        return storeId;
    }

    public BmobDate getOriginTime() {
        return originTime;
    }

    public double getOrderEvaluation() {
        return orderEvaluation;
    }

    public double getPrice() {
        return price;
    }

    public void setOriginTime(BmobDate originTime) {
        this.originTime = originTime;
    }

    public void setEndTime(BmobDate endTime) {
        this.endTime = endTime;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setOrderEvaluation(double orderEvaluation) {
        this.orderEvaluation = orderEvaluation;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }
}
