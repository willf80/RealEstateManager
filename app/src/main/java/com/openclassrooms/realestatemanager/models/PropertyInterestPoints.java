package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        primaryKeys = {"propertyId", "interestId"},
        foreignKeys = {
                @ForeignKey( entity = Property.class,
                        parentColumns = "id",
                        childColumns = "propertyId"),
                @ForeignKey(entity = InterestPoint.class,
                        parentColumns = "id",
                        childColumns = "interestId")
        }
)
public class PropertyInterestPoints {

    private long propertyId;
    private long interestId;

    public PropertyInterestPoints(long propertyId, long interestId) {
        this.propertyId = propertyId;
        this.interestId = interestId;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public long getInterestId() {
        return interestId;
    }

    public void setInterestId(long interestId) {
        this.interestId = interestId;
    }
}
