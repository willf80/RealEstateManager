package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.EstateAgent;

@Dao
public interface EstateAgentDao {

    @Query("SELECT * FROM ESTATEAGENT LIMIT 0, 1")
    LiveData<EstateAgent> getCurrentAgent();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    int setEstateAgent(EstateAgent estateAgent);
}
