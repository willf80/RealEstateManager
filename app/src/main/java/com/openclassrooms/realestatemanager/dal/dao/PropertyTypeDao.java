package com.openclassrooms.realestatemanager.dal.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.PropertyType;

import java.util.List;

@Dao
public abstract class PropertyTypeDao implements BaseDao<PropertyType>{

    @Query("SELECT * FROM PropertyType ORDER BY label ASC")
    public abstract LiveData<List<PropertyType>> getPropertyTypes();

    @Query("SELECT * FROM PropertyType WHERE id = :propertyTypeId")
    public abstract LiveData<PropertyType> getPropertyType(long propertyTypeId);

    // Content provider
    @Query("SELECT * FROM PropertyType WHERE id = :propertyTypeId")
    public abstract Cursor getPropertyTypeWithCursor(long propertyTypeId);
}
