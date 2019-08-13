package com.openclassrooms.realestatemanager.dal.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyDisplayAllInfo;

import java.util.Date;
import java.util.List;

@Dao
public abstract class PropertyDao implements BaseDao<Property>{

    @Query("SELECT * FROM Property ORDER BY createdDate DESC")
    public abstract LiveData<List<Property>> getProperties();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(Property data);

    @Transaction
    @Query("SELECT * FROM Property ORDER BY createdDate DESC")
    public abstract LiveData<List<PropertyDisplayAllInfo>> getPropertyAllDisplayedInfo();

    @Transaction
    @Query("SELECT * FROM Property WHERE id = :propertyId")
    public abstract LiveData<PropertyDisplayAllInfo> getPropertyDisplayedInfo(long propertyId);

    @Query("UPDATE Property SET soldDate = :soldDate, isSold = 1, modifiedDate = :nowDate WHERE id = :propertyId")
    public abstract void update(long propertyId, Date soldDate, Date nowDate);

    // Content provider
    @Query("SELECT * FROM Property ORDER BY createdDate DESC")
    public abstract Cursor getPropertiesWithCursor();
}
