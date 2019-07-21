package com.openclassrooms.realestatemanager.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.AddressProperties;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.Media;
import com.openclassrooms.realestatemanager.models.MediaTemp;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyInfo;
import com.openclassrooms.realestatemanager.models.PropertyInterestPoints;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.repositories.AddressRepository;
import com.openclassrooms.realestatemanager.repositories.InterestPointRepository;
import com.openclassrooms.realestatemanager.repositories.MediaRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyTypeRepository;
import com.openclassrooms.realestatemanager.utils.FileHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    //Repositories
    private final PropertyRepository mPropertySource;
    private final PropertyTypeRepository mTypeSource;
    private final InterestPointRepository mInterestPointRepository;
    private final MediaRepository mMediaRepository;
    private final AddressRepository mAddressRepository;
    private final Executor mExecutor;

    public PropertyViewModel(PropertyRepository propertySource, PropertyTypeRepository typeSource,
                             InterestPointRepository interestPointSource,
                             MediaRepository mediaRepository, AddressRepository addressRepository,
                             Executor executor) {
        mPropertySource = propertySource;
        mTypeSource = typeSource;
        mInterestPointRepository = interestPointSource;
        mMediaRepository = mediaRepository;
        mAddressRepository = addressRepository;
        mExecutor = executor;
    }

    public LiveData<List<Property>> getProperties() {
        return mPropertySource.getAllProperties();
    }

    public void createProperty(Context context, final PropertyInfo propertyInfo) {
        mExecutor.execute(() -> {

            //Save property
            Property property = propertyInfo.property;
            property.setPropertyTypeId(propertyInfo.propertyType.getId());
            property.setUserId(UserViewModel.USER_ID);

            Date now = Calendar.getInstance().getTime();
            property.setCreatedDate(now);
            property.setModifiedDate(now);

            long propertyId = mPropertySource.createProperty(property);

            //Save media
            saveMediaList(context, propertyId, propertyInfo.mediaTempList);

            //Save Address
            saveAddress(propertyInfo.address, propertyId);

            //Save interest points
            saveInterestPoints(propertyInfo.interestPoints, propertyId);
        });
    }

    private void saveInterestPoints(List<InterestPoint> interestPointList, long propertyId){
        for (InterestPoint ip : interestPointList) {
            if (ip.getId() > 0) {
                linkInterestPointWithProperty(ip.getId(), propertyId);
                continue;
            }

            long interestPointId = mInterestPointRepository.createInterestPoint(ip);
            linkInterestPointWithProperty(interestPointId, propertyId);
        }
    }

    private void linkInterestPointWithProperty(long interestPointId, long propertyId) {
        PropertyInterestPoints propertyInterestPoints =
                new PropertyInterestPoints(propertyId, interestPointId);
        mInterestPointRepository.linkInterestPointsWithProperty(propertyInterestPoints);
    }

    private void saveAddress(Address address, long propertyId) {
        //Check if address are existing
        Address existingAddress = mAddressRepository.getAddressLiveData(
                address.getAddressLine1(), address.getPostalCode()).getValue();

        long addressId;
        if(existingAddress == null) {
            addressId = mAddressRepository.createProperty(address);
        }else {
            addressId = existingAddress.getId();
        }
        AddressProperties addressProperties = new AddressProperties(propertyId, addressId);
        mAddressRepository.linkedAddressWithProperty(addressProperties);
    }

    private void saveMediaList(Context context, long propertyId, List<MediaTemp> mediaTemps) {
        if(mediaTemps == null || mediaTemps.size() <= 0) return;

        for (MediaTemp mediaTemp: mediaTemps) {
            //Save photo and get url
            String photoPath = savePhoto(context, mediaTemp.photo);

            //Save media
            Media media = new Media();
            media.setLabel(mediaTemp.description);
            media.setDataPath(photoPath);
            media.setPropertyId(propertyId);

            mMediaRepository.createProperty(media);
        }
    }

    private String savePhoto(Context context, Bitmap bitmap) {
        if(bitmap == null) return null;

        String fileName = UUID.randomUUID().toString();

        FileHelper.saveToInternalStorage(context, bitmap, fileName);

        return fileName;
    }

    public LiveData<List<PropertyType>> getPropertyTypes() {
        return mTypeSource.getAllTypes();
    }

    public LiveData<List<InterestPoint>> getInterestPoints() {
        return mInterestPointRepository.getInterestPoints();
    }
}
