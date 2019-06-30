package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.Address;

@Dao
public abstract class AddressDao implements BaseDao<Address>{

    @Query("SELECT * FROM Address WHERE Address.mNumberAndStreet LIKE :address " +
            "AND Address.mPostalCode = :postalCode")
    public abstract LiveData<Address> searchWithAddressAndPostalCode(String address, String postalCode);

}
