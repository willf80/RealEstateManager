package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PropertyType {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String label;

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
}
