package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.Media;

import java.util.List;

@Dao
public abstract class MediaDao implements BaseDao<Media>{

    @Query("SELECT * FROM Media")
    public abstract LiveData<List<Media>> getMediaList();

    @Query("SELECT * FROM Media WHERE propertyId = :propertyId")
    public abstract LiveData<List<Media>> getMediaList(long propertyId);

    @Query("SELECT * FROM Media WHERE isCover = :isCover AND propertyId = :propertyId")
    public abstract LiveData<Media> getOneMedia(long propertyId, boolean isCover);

    @Query("DELETE FROM Media WHERE propertyId = :propertyId")
    public abstract void deletePropertyMedia(long propertyId);
}
