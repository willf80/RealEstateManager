package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.dal.dao.PropertyTypeDao;
import com.openclassrooms.realestatemanager.models.PropertyType;

import java.util.List;

public class TypeRepository {

    private final PropertyTypeDao mPropertyTypeDao;

    public TypeRepository(PropertyTypeDao propertyTypeDao) {
        mPropertyTypeDao = propertyTypeDao;
    }

    public LiveData<List<PropertyType>> getAllTypes() {
        return mPropertyTypeDao.getPropertyTypes();
    }
}
