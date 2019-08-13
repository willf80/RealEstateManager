package com.openclassrooms.realestatemanager.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.AddressDisplayedInfo;
import com.openclassrooms.realestatemanager.models.AddressProperties;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.Media;
import com.openclassrooms.realestatemanager.models.MediaTemp;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyDisplayAllInfo;
import com.openclassrooms.realestatemanager.models.PropertyInfo;
import com.openclassrooms.realestatemanager.models.PropertyInterestPoints;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.repositories.AddressRepository;
import com.openclassrooms.realestatemanager.repositories.InterestPointRepository;
import com.openclassrooms.realestatemanager.repositories.MediaRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyTypeRepository;
import com.openclassrooms.realestatemanager.repositories.RawQueryRepository;
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
    private final RawQueryRepository mRawQueryRepository;
    private final Executor mExecutor;

    public PropertyViewModel(PropertyRepository propertySource, PropertyTypeRepository typeSource,
                             InterestPointRepository interestPointSource,
                             MediaRepository mediaRepository, AddressRepository addressRepository,
                             RawQueryRepository rawQueryRepository, Executor executor) {
        mPropertySource = propertySource;
        mTypeSource = typeSource;
        mInterestPointRepository = interestPointSource;
        mMediaRepository = mediaRepository;
        mAddressRepository = addressRepository;
        mRawQueryRepository = rawQueryRepository;
        mExecutor = executor;
    }

    public LiveData<List<PropertyDisplayAllInfo>> getAllPropertyDisplayedInfo(){
        return mPropertySource.getAllPropertyDisplayedInfo();
    }

    public LiveData<PropertyDisplayAllInfo> getPropertyDisplayedInfo(long propertyId){
        return mPropertySource.getPropertyDisplayedInfo(propertyId);
    }

    public LiveData<List<Media>> getPropertyMediaList(long propertyId) {
        return mMediaRepository.getMediaList(propertyId);
    }

    public void createProperty(Context context, final PropertyInfo propertyInfo) {
        mExecutor.execute(() -> {

            //Save property
            Property property = propertyInfo.getProperty();
            property.setPropertyTypeId(propertyInfo.getPropertyType().getId());
            property.setUserId(UserViewModel.USER_ID);

            Date now = Calendar.getInstance().getTime();
            property.setCreatedDate(now);
            property.setModifiedDate(now);

            long propertyId = mPropertySource.createProperty(property);

            //Save media
            saveMediaList(context, propertyId, propertyInfo.getMediaTempList());

            //Save Address
            saveAddress(propertyInfo.getAddress(), propertyId);

            //Save interest points
            saveInterestPoints(propertyInfo.getInterestPoints(), propertyId);
        });
    }

    public void markPropertyAsSold(final long propertyId, Date date) {
        mExecutor.execute(() -> {
            mPropertySource.markPropertyAsSold(propertyId, date);
        });
    }

    public void updateProperty(Context context, final long propertyId, final PropertyInfo propertyInfo) {
        mExecutor.execute(() -> {

            // Delete all media, address and property interest points
            deletePropertyMedia(propertyId);
            deleteAddressProperties(propertyId);
            deletePropertyInterestPoints(propertyId);

            // Save media
            saveMediaList(context, propertyId, propertyInfo.getMediaTempList());

            // Save Address
            saveAddress(propertyInfo.getAddress(), propertyId);

            // Save interest points
            saveInterestPoints(propertyInfo.getInterestPoints(), propertyId);

            // Update property
            Property property = propertyInfo.getProperty();
            property.setPropertyTypeId(propertyInfo.getPropertyType().getId());
            property.setUserId(UserViewModel.USER_ID);

            Date now = Calendar.getInstance().getTime();
            property.setModifiedDate(now);
            mPropertySource.updateProperty(property);
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
            addressId = mAddressRepository.createAddress(address);
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
//            Bitmap bitmap = FileHelper.loadImageFromStorage(context, mediaTemp.fileName);
            if(mediaTemp.getFileName() != null && mediaTemp.getFileName().contains("temp_")) {
                FileHelper.deleteFile(context, mediaTemp.getFileName());
            }

            String fileName = savePhoto(context, mediaTemp.getPhoto());

            //Save media
            Media media = new Media();
            media.setLabel(mediaTemp.getLabel());
            media.setFileName(fileName);
            media.setPropertyId(propertyId);
            media.setCover(mediaTemp.isCover());

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

    public LiveData<Media> getSelectedMedia(long propertyId) {
        return mMediaRepository.getSelectedMedia(propertyId);
    }

    public LiveData<AddressDisplayedInfo> getPropertyAddress(long propertyId) {
        return mAddressRepository.getAddress(propertyId);
    }

    public LiveData<PropertyType> getPropertyType(long propertyTypeId) {
        return mTypeSource.getPropertyType(propertyTypeId);
    }

    public LiveData<List<InterestPoint>> getInterestPoints(List<Long> interestPointIds) {
        return mInterestPointRepository.getInterestPoints(interestPointIds);
    }

    public LiveData<List<Long>> getPropertyInterestPointsIds(long propertyId) {
        return mInterestPointRepository.getPropertyInterestPoints(propertyId);
    }

    public LiveData<List<PropertyDisplayAllInfo>> searchProperties(String query, List<Object> params){
        return mRawQueryRepository.searchProperties(query, params);
    }

    private void deletePropertyMedia(long propertyId) {
        mMediaRepository.deletePropertyMedia(propertyId);
    }

    private void deleteAddressProperties(long propertyId) {
        mAddressRepository.deleteAddressProperties(propertyId);
    }

    private void deletePropertyInterestPoints(long propertyId) {
        mInterestPointRepository.deletePropertyInterestPoints(propertyId);
    }
}
