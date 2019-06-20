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
    private int mId;
    private String mLabel;
    private String mDataPath;
    private int mPropertyId;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
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
}
