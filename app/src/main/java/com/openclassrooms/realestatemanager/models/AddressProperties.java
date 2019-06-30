package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        primaryKeys = {"propertyId", "addressId"},
        foreignKeys = {
                @ForeignKey( entity = Property.class,
                        parentColumns = "id",
                        childColumns = "propertyId"),
                @ForeignKey(entity = Address.class,
                        parentColumns = "id",
                        childColumns = "addressId")
        }
)
public class AddressProperties {

    private long propertyId; // PK|FK
    private long addressId; // PK|FK

    public AddressProperties(long propertyId, long addressId) {
        this.propertyId = propertyId;
        this.addressId = addressId;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }
}
