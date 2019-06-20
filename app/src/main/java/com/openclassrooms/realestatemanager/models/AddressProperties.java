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

    private int mPropertyId;
    private int mAddressId;

    public AddressProperties(int propertyId, int addressId) {
        mPropertyId = propertyId;
        mAddressId = addressId;
    }

    public int getPropertyId() {
        return mPropertyId;
    }

    public void setPropertyId(int propertyId) {
        mPropertyId = propertyId;
    }

    public int getAddressId() {
        return mAddressId;
    }

    public void setAddressId(int addressId) {
        mAddressId = addressId;
    }
}
