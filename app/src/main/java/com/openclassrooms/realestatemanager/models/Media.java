package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Property.class,
                parentColumns = "mId",
                childColumns = "mPropertyId")
        )
public class Media {

    @PrimaryKey(autoGenerate = true)
    private long mId;
    private String mLabel;
    private String mDataPath;
    private long mPropertyId; // FK

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getDataPath() {
        return mDataPath;
    }

    public void setDataPath(String dataPath) {
        mDataPath = dataPath;
    }

    public long getPropertyId() {
        return mPropertyId;
    }

    public void setPropertyId(long propertyId) {
        mPropertyId = propertyId;
    }
}
