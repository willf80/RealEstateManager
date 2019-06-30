package com.openclassrooms.realestatemanager.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index("propertyId")},
        foreignKeys = @ForeignKey(entity = Property.class,
                parentColumns = {"id"},
                childColumns = {"propertyId"})
        )
public class Media {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String label;
    private String dataPath;

    @ColumnInfo
    private long propertyId; // FK

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
}
