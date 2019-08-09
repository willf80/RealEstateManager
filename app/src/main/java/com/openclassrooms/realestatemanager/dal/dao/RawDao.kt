package com.openclassrooms.realestatemanager.dal.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.models.PropertyDisplayAllInfo

@Dao
interface RawDao {

    @RawQuery(observedEntities = [PropertyDisplayAllInfo::class])
    fun getProperties(query : SupportSQLiteQuery) : LiveData<List<PropertyDisplayAllInfo>>
}