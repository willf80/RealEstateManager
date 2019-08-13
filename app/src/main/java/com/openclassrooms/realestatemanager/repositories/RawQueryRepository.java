package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.openclassrooms.realestatemanager.dal.dao.RawDao;
import com.openclassrooms.realestatemanager.models.PropertyDisplayAllInfo;

import java.util.List;

public class RawQueryRepository {
    private final RawDao mRawDao;

    public RawQueryRepository(RawDao rawDao) {
        mRawDao = rawDao;
    }

    public LiveData<List<PropertyDisplayAllInfo>> searchProperties(String rawQuery, List<Object> params){

        SimpleSQLiteQuery simpleSQLiteQuery;

        if(params.isEmpty()){
            simpleSQLiteQuery = new SimpleSQLiteQuery(rawQuery);
        }else{
            simpleSQLiteQuery = new SimpleSQLiteQuery(rawQuery, params.toArray());
        }

        return mRawDao.getProperties(simpleSQLiteQuery);
    }
}
