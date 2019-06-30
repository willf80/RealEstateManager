package com.openclassrooms.realestatemanager.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.repositories.InterestPointRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyTypeRepository;
import com.openclassrooms.realestatemanager.repositories.UserRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PropertyRepository mPropertySource;
    private final PropertyTypeRepository mTypeSource;
    private final InterestPointRepository mInterestPointSource;
    private final UserRepository mUserSource;
    private final Executor mExecutor;

    public ViewModelFactory(PropertyRepository propertySource,
                            PropertyTypeRepository typeSource,
                            InterestPointRepository interestPointSource,
                            UserRepository userSource, Executor executor) {
        mPropertySource = propertySource;
        mTypeSource = typeSource;
        mInterestPointSource = interestPointSource;
        mUserSource = userSource;
        mExecutor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(PropertyViewModel.class)) {
            return (T) new PropertyViewModel(mPropertySource, mTypeSource, mInterestPointSource, mExecutor);
        }

        if(modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(mUserSource, mExecutor);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
