package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.PropertyType;

import java.util.List;

@Dao
public abstract class PropertyTypeDao implements BaseDao<PropertyType>{

    @Query("SELECT * FROM PropertyType ORDER BY mLabel ASC")
    public abstract LiveData<List<PropertyType>> getPropertyTypes();

}
