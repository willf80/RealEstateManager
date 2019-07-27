package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.dal.dao.InterestPointDao;
import com.openclassrooms.realestatemanager.dal.dao.PropertyTypeDao;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.PropertyInterestPoints;
import com.openclassrooms.realestatemanager.models.PropertyInterestPointsDisplayInfo;
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

    public long createInterestPoint(InterestPoint interestPoint) {
        return mInterestPointDao.insert(interestPoint);
    }

    public long linkInterestPointsWithProperty(PropertyInterestPoints propertyInterestPoints) {
        return mInterestPointDao.insert(propertyInterestPoints);
    }

    public LiveData<List<InterestPoint>> getInterestPoints(List<Long> interestPointIds) {
        return mInterestPointDao.getInterestPointList(interestPointIds);
    }

    public LiveData<List<Long>> getPropertyInterestPoints(long propertyId) {
        return mInterestPointDao.getPropertyInterestPointsIds(propertyId);
    }


}
