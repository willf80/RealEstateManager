package com.openclassrooms.realestatemanager.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.User;
import com.openclassrooms.realestatemanager.repositories.UserRepository;

import java.util.concurrent.Executor;

public class UserViewModel extends ViewModel {

    public final static int USER_ID = 1;

    //Repositories
    private final UserRepository mUserSource;
    private final Executor mExecutor;

    @Nullable
    private LiveData<User> currentUser;

    public UserViewModel(UserRepository userSource, Executor executor) {
        mUserSource = userSource;
        mExecutor = executor;
    }

    public void init(long userId) {
        if (this.currentUser != null) {
            return;
        }
        currentUser = mUserSource.getUser();
    }

    @Nullable
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
}
