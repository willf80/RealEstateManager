package com.openclassrooms.realestatemanager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.fragments.DetailsPropertyFragment;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.AddressDisplayedInfo;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.Media;
import com.openclassrooms.realestatemanager.models.MediaTemp;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyDisplayAllInfo;
import com.openclassrooms.realestatemanager.models.PropertyInfo;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.utils.FileHelper;

import java.util.List;

public class EditPropertyActivity extends AbstractPropertyEditionActivity {

    private long mPropertyId;
    private PropertyInfo mPropertyInfo;

    private LiveData<AddressDisplayedInfo> mAddressDisplayedInfoLiveData;
    private LiveData<PropertyDisplayAllInfo> mPropertyDisplayAllInfoLiveData;
    private LiveData<List<Long>> mPropertyInterestPointsIdsLiveData;
    private LiveData<List<Media>> mPropertyMediaListLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Edit property");

        mPropertyId = getIntent()
                .getLongExtra(DetailsPropertyFragment.ARG_PROPERTY_ID, 0);

        onPreloadData();
    }

    @Override
    public Mode definedMode() {
        return Mode.Modification;
    }

    public void onPreloadData() {
        // Load property
        mPropertyDisplayAllInfoLiveData = mPropertyViewModel.getPropertyDisplayedInfo(mPropertyId);
        mPropertyDisplayAllInfoLiveData.observeForever(this::fetchAllPropertyInfo);
    }

    @SuppressLint("SetTextI18n")
    private void fetchAllPropertyInfo(PropertyDisplayAllInfo displayAllInfo) {
        if(displayAllInfo == null ) return;

        Property property = displayAllInfo.getProperty();
        if(property == null ) return;

        descriptionEditText.setText(property.getDescription());
        surfaceEditText.setText(property.getArea() + "");
        numberOfRoomsEditText.setText(property.getNumberOfRooms() + "");
        numberOfBathroomsEditText.setText(property.getNumberOfBathrooms() + "");
        numberOfBedroomsEditText.setText(property.getNumberOfBedrooms() + "");
        priceEditText.setText(property.getPrice() + "");
        addressLine2EditText.setText(property.getAddressLine2());

        for (Media media : displayAllInfo.getMediaList()) {
            MediaTemp mediaTemp = new MediaTemp();
            mediaTemp.setId(media.getId());
            mediaTemp.setFileName(media.getFileName());
            mediaTemp.setLabel(media.getLabel());
            mediaTemp.setCover(media.isCover());
            mediaTemp.setPhoto(FileHelper.loadImageFromStorage(this, media.getFileName()));

            mMediaBoxView.addMedia(mediaTemp);
        }

        mAddressDisplayedInfoLiveData = mPropertyViewModel.getPropertyAddress(property.getId());
        mAddressDisplayedInfoLiveData.observeForever(this::fetchAddress);

        mPropertyInterestPointsIdsLiveData =
                mPropertyViewModel.getPropertyInterestPointsIds(property.getId());
        mPropertyInterestPointsIdsLiveData.observe(this, this::fetchPropertyInterestPointsIds);

        mPropertyViewModel.getPropertyType(property.getPropertyTypeId())
                .observe(this, this::fetchPropertyType);

        // Remove observer
        if(mPropertyDisplayAllInfoLiveData != null) {
            mPropertyDisplayAllInfoLiveData.removeObservers(this);
        }
    }

    private void fetchAddress(AddressDisplayedInfo addressInfo) {
        if(addressInfo == null || addressInfo.getAddress().size() <= 0) return;

        Address address = addressInfo.getAddress().get(0);
        addressLine1EditText.setText(address.getAddressLine1());
        postalCodeEditText.setText(address.getPostalCode());

        // Remove observer
        if(mAddressDisplayedInfoLiveData != null) {
            mAddressDisplayedInfoLiveData.removeObservers(this);
        }
    }

    private void fetchPropertyInterestPointsIds(List<Long> propertyInterestPointIds) {
        mPropertyViewModel.getInterestPoints(propertyInterestPointIds)
                .observe(this, this::fetchInterestPoints);

        //Remove observer
        if(mPropertyInterestPointsIdsLiveData != null) {
            mPropertyInterestPointsIdsLiveData.removeObservers(this);
        }
    }

    private void fetchInterestPoints(List<InterestPoint> interestPointList) {
        for (InterestPoint interestPoint : interestPointList) {
            mInterestPointsAddingView.addTag(interestPoint.getLabel());
        }
    }

    private void fetchPropertyType(PropertyType propertyType) {
        int index = 0;

        for (int i = 0; i < mPropertyTypeList.size(); i++) {
            PropertyType pt = mPropertyTypeList.get(i);
            if(pt.getId() != propertyType.getId()) continue;

            index = i;
            break;
        }

        mPropertyTypeSpinner.setSelection(index);
    }


    @Override
    public void save(PropertyInfo propertyInfo) {
        mPropertyInfo = propertyInfo;
        mPropertyInfo.getProperty().setId(mPropertyId);

        mPropertyMediaListLiveData = mPropertyViewModel.getPropertyMediaList(mPropertyId);
        mPropertyMediaListLiveData.observe(this, this::fetchPropertyMediaList);
    }

    private void fetchPropertyMediaList(List<Media> mediaList) {
        //Delete all media file on internal data storage
        for (Media media : mediaList) {
            FileHelper.deleteFile(this, media.getFileName());
        }

        if(mPropertyMediaListLiveData != null) {
            mPropertyMediaListLiveData.removeObservers(this);
        }

        updateProperty();
    }

    private void updateProperty() {
        //Update property
        mPropertyViewModel.updateProperty(this, mPropertyId, mPropertyInfo);
        finish();
    }
}
