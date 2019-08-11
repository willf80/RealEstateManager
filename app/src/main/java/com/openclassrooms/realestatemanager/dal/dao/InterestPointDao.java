package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.PropertyInterestPoints;
import com.openclassrooms.realestatemanager.models.PropertyInterestPointsDisplayInfo;

import java.util.List;

@Dao
public abstract class InterestPointDao implements BaseDao<InterestPoint>{

    @Query("SELECT * FROM InterestPoint ORDER BY label ASC")
    public abstract LiveData<List<InterestPoint>> getInterestPointList();

    @Query("SELECT * FROM InterestPoint WHERE id IN (:interestPointIds)")
    public abstract LiveData<List<InterestPoint>> getInterestPointList(List<Long> interestPointIds);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(PropertyInterestPoints propertyInterestPoints);

    @Query("SELECT interestId FROM PropertyInterestPoints WHERE propertyId = :propertyId")
    public abstract LiveData<List<Long>> getPropertyInterestPointsIds(long propertyId);

    @Query("DELETE FROM PropertyInterestPoints WHERE propertyId = :propertyId")
    public abstract void deletePropertyInterestPoints(long propertyId);
}
