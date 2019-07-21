package com.openclassrooms.realestatemanager.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.repositories.InterestPointRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyTypeRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    //Repositories
    private final PropertyRepository mPropertySource;
    private final PropertyTypeRepository mTypeSource;
    private final InterestPointRepository mInterestPointRepository;
    private final Executor mExecutor;

    public PropertyViewModel(PropertyRepository propertySource,
                             PropertyTypeRepository typeSource,
                             InterestPointRepository interestPointSource, Executor executor) {
        mPropertySource = propertySource;
        mTypeSource = typeSource;
        mInterestPointRepository = interestPointSource;
        mExecutor = executor;
    }

    public LiveData<List<Property>> getProperties() {
        return mPropertySource.getAllProperties();
    }

    public void createProperty(Property property) {
        mExecutor.execute(() -> mPropertySource.createProperty(property));
    }

    public LiveData<List<PropertyType>> getPropertyTypes() {
        return mTypeSource.getAllTypes();
    }

    public LiveData<List<InterestPoint>> getInterestPoints() {
        return mInterestPointRepository.getInterestPoints();
    }
}
