package com.openclassrooms.realestatemanager.injection;

import android.content.Context;

import com.openclassrooms.realestatemanager.dal.AppDatabase;
import com.openclassrooms.realestatemanager.repositories.InterestPointRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyTypeRepository;
import com.openclassrooms.realestatemanager.repositories.UserRepository;
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static PropertyRepository providePropertyDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new PropertyRepository(appDatabase.propertyDao());
    }

    public static PropertyTypeRepository providePropertyTypeDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new PropertyTypeRepository(appDatabase.propertyTypeDao());
    }

    public static InterestPointRepository provideInterestPointDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new InterestPointRepository(appDatabase.interestPointDao());
    }

    public static UserRepository provideUserDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new UserRepository(appDatabase.userDao());
    }

    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        PropertyRepository dataSourceProperty = providePropertyDataSource(context);
        UserRepository dataSourceUser = provideUserDataSource(context);
        PropertyTypeRepository dataSourcePropertyType = providePropertyTypeDataSource(context);
        InterestPointRepository dataSourceInterestPoint = provideInterestPointDataSource(context);

        Executor executor = provideExecutor();

        return new ViewModelFactory(dataSourceProperty, dataSourcePropertyType,
                dataSourceInterestPoint, dataSourceUser, executor);
    }

}
