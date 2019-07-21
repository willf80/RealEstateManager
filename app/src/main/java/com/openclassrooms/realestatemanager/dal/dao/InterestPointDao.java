package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.PropertyInterestPoints;

import java.util.List;

@Dao
public abstract class InterestPointDao implements BaseDao<InterestPoint>{

    @Query("SELECT * FROM InterestPoint")
    public abstract LiveData<List<InterestPoint>> getInterestPointList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(PropertyInterestPoints propertyInterestPoints);
}
