package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = PropertyType.class,
        parentColumns = "mId",
        childColumns = "mPropertyTypeId"),

        @ForeignKey(entity = User.class,
        parentColumns = "mId",
        childColumns = "mUserId")
    }
)
public class Property {

    @PrimaryKey(autoGenerate = true)
    private long mId;

    private double mPrice;
    private double mArea;
    private int mNumberOfRooms;
    private int mNumberOfBathrooms;
    private int mNumberOfBedrooms;
    private String mDescription;
    private String mAddress;
    private boolean mStatus;
    private String mEntryOfMarketDate;
    private String mSaleDate;
    private long mPropertyTypeId; //FK
    private long mUserId; //FK

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getPropertyTypeId() {
        return mPropertyTypeId;
    }

    public void setPropertyTypeId(long propertyTypeId) {
        mPropertyTypeId = propertyTypeId;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public double getArea() {
        return mArea;
    }

    public void setArea(double area) {
        mArea = area;
    }

    public int getNumberOfRooms() {
        return mNumberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        mNumberOfRooms = numberOfRooms;
    }

    public int getNumberOfBathrooms() {
        return mNumberOfBathrooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        mNumberOfBathrooms = numberOfBathrooms;
    }

    public int getNumberOfBedrooms() {
        return mNumberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        mNumberOfBedrooms = numberOfBedrooms;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public boolean isStatus() {
        return mStatus;
    }

    public void setStatus(boolean status) {
        mStatus = status;
    }

    public String getEntryOfMarketDate() {
        return mEntryOfMarketDate;
    }

    public void setEntryOfMarketDate(String entryOfMarketDate) {
        mEntryOfMarketDate = entryOfMarketDate;
    }

    public String getSaleDate() {
        return mSaleDate;
    }

    public void setSaleDate(String saleDate) {
        mSaleDate = saleDate;
    }

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }
}
