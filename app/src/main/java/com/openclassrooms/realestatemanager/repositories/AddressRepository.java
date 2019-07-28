package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.dal.dao.AddressDao;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.AddressDisplayedInfo;
import com.openclassrooms.realestatemanager.models.AddressProperties;
import com.openclassrooms.realestatemanager.models.PropertyInterestPoints;

public class AddressRepository {

    private final AddressDao mAddressDao;

    public AddressRepository(AddressDao addressDao) {
        mAddressDao = addressDao;
    }

    public LiveData<Address> getAddressLiveData(String address, String postalCode) {
        return mAddressDao.searchWithAddressAndPostalCode(address, postalCode);
    }

    public long createAddress(Address address) {
        return mAddressDao.insert(address);
    }

    public void deleteAddressProperties(long propertyId) {
        mAddressDao.deleteAddressProperties(propertyId);
    }

    public long linkedAddressWithProperty(AddressProperties addressProperty) {
        return mAddressDao.insert(addressProperty);
    }

    public LiveData<AddressDisplayedInfo> getAddress(long propertyId) {
        return mAddressDao.getAddressWithPropertyId(propertyId);
    }
}
