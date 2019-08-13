package com.openclassrooms.realestatemanager.injection;

import android.content.Context;

import com.openclassrooms.realestatemanager.dal.AppDatabase;
import com.openclassrooms.realestatemanager.repositories.AddressRepository;
import com.openclassrooms.realestatemanager.repositories.InterestPointRepository;
import com.openclassrooms.realestatemanager.repositories.MediaRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyTypeRepository;
import com.openclassrooms.realestatemanager.repositories.RawQueryRepository;
import com.openclassrooms.realestatemanager.repositories.UserRepository;
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    private static RawQueryRepository rawQueryDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new RawQueryRepository(appDatabase.rowDao());
    }

    private static PropertyRepository providePropertyDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new PropertyRepository(appDatabase.propertyDao());
    }

    private static PropertyTypeRepository providePropertyTypeDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new PropertyTypeRepository(appDatabase.propertyTypeDao());
    }

    private static InterestPointRepository provideInterestPointDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new InterestPointRepository(appDatabase.interestPointDao());
    }

    private static UserRepository provideUserDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new UserRepository(appDatabase.userDao());
    }

    private static MediaRepository provideMediaDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new MediaRepository(appDatabase.mediaDao());
    }

    private static AddressRepository provideAddressDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new AddressRepository(appDatabase.addressDao());
    }

    private static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        PropertyRepository dataSourceProperty = providePropertyDataSource(context);
        UserRepository dataSourceUser = provideUserDataSource(context);
        PropertyTypeRepository dataSourcePropertyType = providePropertyTypeDataSource(context);
        InterestPointRepository dataSourceInterestPoint = provideInterestPointDataSource(context);
        MediaRepository dataSourceMedia = provideMediaDataSource(context);
        AddressRepository dataSourceAddress = provideAddressDataSource(context);
        RawQueryRepository dataSourceRawQuery = rawQueryDataSource(context);

        Executor executor = provideExecutor();

        return new ViewModelFactory(dataSourceProperty, dataSourcePropertyType,
                dataSourceInterestPoint, dataSourceUser, dataSourceMedia,
                dataSourceAddress, dataSourceRawQuery, executor);
    }

}
