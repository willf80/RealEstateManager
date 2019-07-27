package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.PropertyInterestPoints;
import com.openclassrooms.realestatemanager.models.PropertyInterestPointsDisplayInfo;

@Dao
public abstract class PropertyInterestPointsDao implements BaseDao<PropertyInterestPoints>{

    @Query("SELECT * FROM PropertyInterestPoints WHERE propertyId = :propertyId")
    public abstract LiveData<PropertyInterestPointsDisplayInfo> getPropertyInterestPointList(long propertyId);

}
