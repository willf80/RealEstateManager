package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        primaryKeys = {"mPropertyId", "mAddressId"},
        foreignKeys = {
                @ForeignKey( entity = Property.class,
                        parentColumns = "mId",
                        childColumns = "mPropertyId"),
                @ForeignKey(entity = Address.class,
                        parentColumns = "mId",
                        childColumns = "mAddressId")
        }
)
public class AddressProperties {

    private long mPropertyId; // PK|FK
    private long mAddressId; // PK|FK

    public AddressProperties(long propertyId, long addressId) {
        mPropertyId = propertyId;
        mAddressId = addressId;
    }

    public long getPropertyId() {
        return mPropertyId;
    }

    public void setPropertyId(long propertyId) {
        mPropertyId = propertyId;
    }

    public long getAddressId() {
        return mAddressId;
    }

    public void setAddressId(long addressId) {
        mAddressId = addressId;
    }
}
