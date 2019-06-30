package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Type.class,
        parentColumns = "mId",
        childColumns = "mTypeId"),

        @ForeignKey(entity = EstateAgent.class,
        parentColumns = "mId",
        childColumns = "mEstateAgentId")
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
    private long mTypeId; //FK
    private long mEstateAgentId; //FK

    public long getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public long getTypeId() {
        return mTypeId;
    }

    public void setTypeId(long typeId) {
        mTypeId = typeId;
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

    public long getEstateAgentId() {
        return mEstateAgentId;
    }

    public void setEstateAgentId(long estateAgentId) {
        mEstateAgentId = estateAgentId;
    }
}
