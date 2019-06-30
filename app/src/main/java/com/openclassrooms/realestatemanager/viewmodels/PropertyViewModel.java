package com.openclassrooms.realestatemanager.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;
import com.openclassrooms.realestatemanager.repositories.TypeRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    //Repositories
    private final PropertyRepository mPropertySource;
    private final TypeRepository mTypeSource;
    private final Executor mExecutor;

    public PropertyViewModel(PropertyRepository propertySource,
                             TypeRepository typeSource,
                             Executor executor) {
        mPropertySource = propertySource;
        mTypeSource = typeSource;
        mExecutor = executor;
    }

    public LiveData<List<Property>> getProperties() {
        return mPropertySource.getAllProperties();
    }

    public void createProperty(Property property) {
        mExecutor.execute(() -> {
            mPropertySource.createProperty(property);
        });
    }
}
