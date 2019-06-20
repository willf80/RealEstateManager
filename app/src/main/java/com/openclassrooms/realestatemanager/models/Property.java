package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

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
    private int mId;

    private int mTypeId;
    private double mPrice;
    private double mArea;
    private int mNumberOfRooms;
    private int mNumberOfBathrooms;
    private int mNumberOfBedrooms;
    private String mDescription;
    private String mAddress;
    private List<InterestPoint> mInterestPointList;
    private boolean mStatus;
    private String mEntryOfMarketDate;
    private String mSaleDate;
    private int mEstateAgentId;

}
