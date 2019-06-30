package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.User;

@Dao
public abstract class UserDao implements BaseDao<User> {

    @Query("SELECT * FROM User LIMIT 0, 1")
    public abstract LiveData<User> getCurrentUser();
}
