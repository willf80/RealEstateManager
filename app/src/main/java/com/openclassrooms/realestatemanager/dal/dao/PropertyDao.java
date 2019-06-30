package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

@Dao
public abstract class PropertyDao implements BaseDao<Property>{

    @Query("SELECT * FROM Property")
    public abstract LiveData<List<Property>> getProperties();

}
