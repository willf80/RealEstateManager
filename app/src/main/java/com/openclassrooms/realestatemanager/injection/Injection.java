package com.openclassrooms.realestatemanager.injection;

import android.content.Context;

import com.openclassrooms.realestatemanager.dal.AppDatabase;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyTypeRepository;

public class Injection {

    public static PropertyRepository providePropertyDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new PropertyRepository(appDatabase.propertyDao());
    }

    public static PropertyTypeRepository providePropertyTypeDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new PropertyTypeRepository(appDatabase.propertyTypeDao());
    }

}
