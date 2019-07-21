package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.dal.dao.PropertyDao;
import com.openclassrooms.realestatemanager.dal.dao.PropertyTypeDao;
import com.openclassrooms.realestatemanager.dal.dao.UserDao;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.models.User;

import java.util.List;

public class PropertyRepository {

    private final PropertyDao mPropertyDao;
    private UserDao mUserDao;
    private PropertyTypeDao mPropertyTypeDao;

    public PropertyRepository(PropertyDao propertyDao) {
        mPropertyDao = propertyDao;
    }

//    public PropertyRepository(PropertyDao propertyDao, UserDao userDao, PropertyTypeDao propertyTypeDao) {
//        mPropertyDao = propertyDao;
//        mUserDao = userDao;
//        mPropertyTypeDao = propertyTypeDao;
//    }

    public LiveData<List<Property>> getAllProperties() {
        return mPropertyDao.getProperties();
    }

    public long createProperty(Property property) {
        return mPropertyDao.insert(property);
    }

//    public long createProperty(Property property, User user, PropertyType type) {
//        if(mUserDao == null || mPropertyTypeDao == null) {
//            throw new NullPointerException("UserDao or PropertyTypeDao are not initialized");
//        }
//
//        long userId = mUserDao.insert(user);
//        long typeId = mPropertyTypeDao.insert(type);
//
//        property.setUserId(userId);
//        property.setPropertyTypeId(typeId);
//
//        return mPropertyDao.insert(property);
//    }
}
