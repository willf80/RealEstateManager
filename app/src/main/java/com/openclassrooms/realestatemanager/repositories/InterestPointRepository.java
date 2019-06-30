package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.dal.dao.InterestPointDao;
import com.openclassrooms.realestatemanager.dal.dao.PropertyTypeDao;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.PropertyType;

import java.util.List;

public class InterestPointRepository {

    private final InterestPointDao mInterestPointDao;

    public InterestPointRepository(InterestPointDao interestPointDao) {
        mInterestPointDao = interestPointDao;
    }

    public LiveData<List<InterestPoint>> getInterestPoints() {
        return mInterestPointDao.getInterestPointList();
    }
}
