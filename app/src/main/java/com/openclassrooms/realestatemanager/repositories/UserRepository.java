package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.dal.dao.PropertyTypeDao;
import com.openclassrooms.realestatemanager.dal.dao.UserDao;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.models.User;

import java.util.List;

public class UserRepository {

    private final UserDao mUserDao;

    public UserRepository(UserDao userDao) {
        mUserDao = userDao;
    }

    public LiveData<User> getUser() {
        return mUserDao.getCurrentUser();
    }
}
