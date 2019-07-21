package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyAllDisplayedInfo;
import com.openclassrooms.realestatemanager.models.PropertyDisplayedInfo;

import java.util.List;

@Dao
public abstract class PropertyDao implements BaseDao<Property>{

    @Query("SELECT * FROM Property ")
    public abstract LiveData<List<Property>> getProperties();

    @Transaction
    @Query("SELECT * FROM Property")
    public abstract LiveData<List<PropertyAllDisplayedInfo>> getPropertyAllDisplayedInfo();

    @Transaction
    @Query("SELECT id, area, status FROM Property")
    public abstract LiveData<List<PropertyDisplayedInfo>> getPropertyDisplayedInfo();

    @Transaction
    @Query("SELECT * FROM Property WHERE id = :propertyId")
    public abstract LiveData<PropertyAllDisplayedInfo> getPropertyDisplayedInfo(long propertyId);
}
