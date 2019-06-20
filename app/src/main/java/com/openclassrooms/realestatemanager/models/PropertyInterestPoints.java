package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        primaryKeys = {"mPropertyId", "mInterestId"},
        foreignKeys = {
                @ForeignKey( entity = Property.class,
                        parentColumns = "mId",
                        childColumns = "mPropertyId"),
                @ForeignKey(entity = InterestPoint.class,
                        parentColumns = "mId",
                        childColumns = "mInterestId")
        }
)
public class PropertyInterestPoints {

    private int mPropertyId;
    private int mInterestId;

    public PropertyInterestPoints(int propertyId, int interestId) {
        mPropertyId = propertyId;
        mInterestId = interestId;
    }

    public int getPropertyId() {
        return mPropertyId;
    }

    public void setPropertyId(int propertyId) {
        mPropertyId = propertyId;
    }

    public int getInterestId() {
        return mInterestId;
    }

    public void setInterestId(int interestId) {
        mInterestId = interestId;
    }
}
