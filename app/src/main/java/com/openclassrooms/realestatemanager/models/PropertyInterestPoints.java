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

    private long mPropertyId;
    private long mInterestId;

    public PropertyInterestPoints(long propertyId, long interestId) {
        mPropertyId = propertyId;
        mInterestId = interestId;
    }

    public long getPropertyId() {
        return mPropertyId;
    }

    public void setPropertyId(long propertyId) {
        mPropertyId = propertyId;
    }

    public long getInterestId() {
        return mInterestId;
    }

    public void setInterestId(long interestId) {
        mInterestId = interestId;
    }
}
