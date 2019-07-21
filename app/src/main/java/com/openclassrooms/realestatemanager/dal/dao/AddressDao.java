package com.openclassrooms.realestatemanager.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.AddressProperties;

@Dao
public abstract class AddressDao implements BaseDao<Address>{

    @Query("SELECT * FROM Address WHERE Address.addressLine1 LIKE :address " +
            "AND Address.postalCode = :postalCode")
    public abstract LiveData<Address> searchWithAddressAndPostalCode(String address, String postalCode);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(AddressProperties addressProperties);

    @Delete
    public abstract void delete(AddressProperties addressProperties);
}
