package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index("propertyTypeId"), @Index("userId")},
        foreignKeys = {
        @ForeignKey(entity = PropertyType.class,
        parentColumns = "id",
        childColumns = "propertyTypeId"),

        @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId")
    }
)
public class Property {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private double price;
    private double area;
    private int numberOfRooms;
    private int numberOfBathrooms;
    private int numberOfBedrooms;
    private String description;
    private String addressLine2;
    private boolean status;
    private String entryOfMarketDate;
    private String saleDate;
    private long propertyTypeId; //FK
    private long userId; //FK

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(long propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getEntryOfMarketDate() {
        return entryOfMarketDate;
    }

    public void setEntryOfMarketDate(String entryOfMarketDate) {
        this.entryOfMarketDate = entryOfMarketDate;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
