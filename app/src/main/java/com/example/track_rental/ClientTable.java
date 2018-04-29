package com.example.track_rental;

import cn.bmob.v3.BmobObject;

/**
 * Created by 叶泽锐 on 2018/4/23.
 */

public class ClientTable extends BmobObject{
    private String phoneNumber;

    private String Key;

    private double cashPledge;

    public ClientTable(String phoneNumber, String Key, double cashPledge){
        this.cashPledge = cashPledge;
        this.Key = Key;
        this.phoneNumber = phoneNumber;
    }

    public double getCashPledge() {
        return cashPledge;
    }

    public String getKey() {
        return Key;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setCashPledge(double cashPledge) {
        this.cashPledge = cashPledge;
    }

    public void setKey(String key) {
        Key = key;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
