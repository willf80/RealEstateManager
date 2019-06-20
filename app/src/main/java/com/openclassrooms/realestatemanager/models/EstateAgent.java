package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EstateAgent {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    private String mFullName;
    private String mEmail;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }
}
