package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.InterestPoint;

import java.util.List;

@Dao
public abstract class InterestPointDao implements BaseDao<InterestPoint>{

    @Query("SELECT * FROM InterestPoint")
    public abstract LiveData<List<InterestPoint>> getInterestPointList();

}
