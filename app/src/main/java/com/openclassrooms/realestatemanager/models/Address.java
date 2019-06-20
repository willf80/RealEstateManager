package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Address {

    @PrimaryKey(autoGenerate = true)
    private int mId;

    private String mNumberAndStreet;

    private String mPostalCode;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getNumberAndStreet() {
        return mNumberAndStreet;
    }

    public void setNumberAndStreet(String numberAndStreet) {
        mNumberAndStreet = numberAndStreet;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(String postalCode) {
        mPostalCode = postalCode;
    }
}
