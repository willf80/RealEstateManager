package com.openclassrooms.realestatemanager.models;

import java.util.List;

public class Property {
    private String mId;
    private Type mType;
    private double mPrice;
    private double mArea;
    private int mNumberOfRooms;
    private int mNumberOfBathrooms;
    private int mNumberOfBedrooms;
    private String mDescription;
    private List<Media> mMediaList;
    private String mAddress;
    private List<InterestPoint> mInterestPointList;
    private boolean mStatus;
    private String mEntryOfMarketDate;
    private String mSaleDate;
    private EstateAgent mEstateAgent;

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public List<Media> getMediaList() {
        return mMediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        mMediaList = mediaList;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public List<InterestPoint> getInterestPointList() {
        return mInterestPointList;
    }

    public void setInterestPointList(List<InterestPoint> interestPointList) {
        mInterestPointList = interestPointList;
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

    public EstateAgent getEstateAgent() {
        return mEstateAgent;
    }

    public void setEstateAgent(EstateAgent estateAgent) {
        mEstateAgent = estateAgent;
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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
