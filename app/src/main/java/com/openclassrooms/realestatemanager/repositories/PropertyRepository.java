package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.dal.dao.PropertyDao;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyAllDisplayedInfo;

import java.util.List;

public class PropertyRepository {

    private final PropertyDao mPropertyDao;

    public PropertyRepository(PropertyDao propertyDao) {
        mPropertyDao = propertyDao;
    }

    public LiveData<List<Property>> getAllProperties() {
        return mPropertyDao.getProperties();
    }

    public LiveData<List<PropertyAllDisplayedInfo>> getAllPropertyDisplayedInfo(){
        return mPropertyDao.getPropertyAllDisplayedInfo();
    }

    public long createProperty(Property property) {
        return mPropertyDao.insert(property);
    }

    public LiveData<PropertyAllDisplayedInfo> getPropertyDisplayedInfo(long propertyId) {
        return mPropertyDao.getPropertyDisplayedInfo(propertyId);
    }
}
