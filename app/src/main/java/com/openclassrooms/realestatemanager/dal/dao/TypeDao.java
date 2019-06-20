package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.Type;

import java.util.List;

@Dao
public interface TypeDao {

    @Query("SELECT * FROM Type")
    LiveData<List<Type>> getTypes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertType(Type type);

}
