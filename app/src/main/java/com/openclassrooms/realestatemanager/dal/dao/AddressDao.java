package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.Address;

@Dao
public interface AddressDao {

    @Query("SELECT * FROM Address WHERE Address.mNumberAndStreet LIKE :address " +
            "AND Address.mPostalCode = :postalCode")
    LiveData<Address> searchWithAddressAndPostalCode(String address, String postalCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAddress(Address address);

}
