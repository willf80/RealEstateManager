package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.models.Media;
import com.openclassrooms.realestatemanager.models.Type;

import java.util.List;

@Dao
public interface MediaDao {

    @Query("SELECT * FROM Media")
    LiveData<List<Media>> getMediaList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMedia(Media media);

    @Update
    int updateMedia(Media media);

    @Query("DELETE FROM Media WHERE mId = :mediaId")
    int deleteMedia(long mediaId);
}
