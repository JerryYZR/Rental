package com.example.track_rental;

import java.lang.ref.SoftReference;

import cn.bmob.v3.BmobObject;

/**
 * Created by 叶泽锐 on 2018/4/23.
 */

public class TenantTable extends BmobObject{
    private String storeName;
    private String storeId;
    private String phoneNumber;
    private float evaluation;
    private String address;
    private String city;
    private String area;
    private int areaId;
    private boolean sendOrNot;


    public TenantTable(String storeId, String storeName, String phoneNumber,int areaId,
                       float evaluation, String address, String city, String area, boolean sendOrNot){
        this.storeId = storeId;
        this.storeName = storeName;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.evaluation = evaluation;
        this.area = area;
        this.address = address;
        this.areaId = areaId;
        this.sendOrNot = sendOrNot;
    }

    public void setSendOrNot(boolean sendOrNot) {
        this.sendOrNot = sendOrNot;
    }

    public boolean getSendOrNot(){
        return sendOrNot;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public float getEvaluation() {
        return evaluation;
    }

    public String getAddress() {
        return address;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEvaluation(float evaluation) {
        this.evaluation = evaluation;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}

