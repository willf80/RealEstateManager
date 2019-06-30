package com.openclassrooms.realestatemanager.dal.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDao<T> {

    @Insert
    long insert(T data);

    @Update
    int update(T data);

    @Delete
    int delete(T data);

}
