package com.openclassrooms.realestatemanager.services;

import androidx.annotation.NonNull;

public class ServiceFactory {
    private final InterestPointsAddingViewService mPointsAddingViewService;

    public ServiceFactory(InterestPointsAddingViewService pointsAddingViewService) {
        mPointsAddingViewService = pointsAddingViewService;
    }

    public <T> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(InterestPointsAddingViewService.class)){
            return (T) new InterestPointsAddingViewService();
        }

        throw new IllegalArgumentException("Unknown Service class");
    }
}
