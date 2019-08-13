package com.openclassrooms.realestatemanager.injection;

import com.openclassrooms.realestatemanager.services.InterestPointsAddingViewService;
import com.openclassrooms.realestatemanager.services.ServiceFactory;

public class ServicesInjection {

    public static ServiceFactory provideServiceFactory(){
        InterestPointsAddingViewService pointsAddingViewService = new InterestPointsAddingViewService();

        return new ServiceFactory(pointsAddingViewService);
    }

}
