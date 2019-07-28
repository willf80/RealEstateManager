package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.dal.dao.InterestPointDao;
import com.openclassrooms.realestatemanager.dal.dao.MediaDao;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.Media;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

public class MediaRepository {

    private final MediaDao mMediaDao;

    public MediaRepository(MediaDao mediaDao) {
        mMediaDao = mediaDao;
    }

    public LiveData<List<Media>> getMediaList() {
        return mMediaDao.getMediaList();
    }

    public LiveData<List<Media>> getMediaList(long propertyId) {
        return mMediaDao.getMediaList(propertyId);
    }

    public long createProperty(Media media) {
        return mMediaDao.insert(media);
    }

    public void deletePropertyMedia(long propertyId) {
        mMediaDao.deletePropertyMedia(propertyId);
    }

    public LiveData<Media> getSelectedMedia(long propertyId) {
        return mMediaDao.getOneMedia(propertyId, true);
    }
}
