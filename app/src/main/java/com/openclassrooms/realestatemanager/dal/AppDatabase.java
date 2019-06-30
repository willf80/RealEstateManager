package com.openclassrooms.realestatemanager.dal;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.openclassrooms.realestatemanager.dal.dao.AddressDao;
import com.openclassrooms.realestatemanager.dal.dao.PropertyTypeDao;
import com.openclassrooms.realestatemanager.dal.dao.UserDao;
import com.openclassrooms.realestatemanager.dal.dao.InterestPointDao;
import com.openclassrooms.realestatemanager.dal.dao.MediaDao;
import com.openclassrooms.realestatemanager.dal.dao.PropertyDao;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.AddressProperties;
import com.openclassrooms.realestatemanager.models.User;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.Media;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyInterestPoints;
import com.openclassrooms.realestatemanager.models.PropertyType;

@Database(version = 1,
        entities = {
                Address.class, User.class, InterestPoint.class,
                Media.class, Property.class, PropertyType.class,
                PropertyInterestPoints.class, AddressProperties.class
        },
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "AppDatabase.db";

    // SINGLETON
    private static volatile AppDatabase INSTANCE;

    // DAO
    public abstract AddressDao addressDao();
    public abstract UserDao userDao();
    public abstract InterestPointDao interestPointDao();
    public abstract MediaDao mediaDao();
    public abstract PropertyDao propertyDao();
    public abstract PropertyTypeDao propertyTypeDao();

    // INSTANCE
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = create(context);
        }

        return INSTANCE;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME).build();
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
