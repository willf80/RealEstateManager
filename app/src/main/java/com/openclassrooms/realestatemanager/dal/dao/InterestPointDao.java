package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.Type;

import java.util.List;

@Dao
public interface InterestPointDao {

    @Query("SELECT * FROM Type")
    LiveData<List<InterestPoint>> getInterestPointList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertInterestPoint(InterestPoint interestPoint);

}
