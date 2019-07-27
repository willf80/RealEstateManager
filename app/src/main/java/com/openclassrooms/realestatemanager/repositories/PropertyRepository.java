package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.dal.dao.PropertyDao;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyDisplayAllInfo;

import java.util.List;

public class PropertyRepository {

    private final PropertyDao mPropertyDao;

    public PropertyRepository(PropertyDao propertyDao) {
        mPropertyDao = propertyDao;
    }

    public LiveData<List<Property>> getAllProperties() {
        return mPropertyDao.getProperties();
    }

    public LiveData<List<PropertyDisplayAllInfo>> getAllPropertyDisplayedInfo(){
        return mPropertyDao.getPropertyAllDisplayedInfo();
    }

    public long createProperty(Property property) {
        return mPropertyDao.insert(property);
    }

    public LiveData<PropertyDisplayAllInfo> getPropertyDisplayedInfo(long propertyId) {
        return mPropertyDao.getPropertyDisplayedInfo(propertyId);
    }
}
