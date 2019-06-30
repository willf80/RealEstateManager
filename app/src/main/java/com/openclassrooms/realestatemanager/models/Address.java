package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Address {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String numberAndStreet;

    private String postalCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumberAndStreet() {
        return numberAndStreet;
    }

    public void setNumberAndStreet(String numberAndStreet) {
        this.numberAndStreet = numberAndStreet;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
