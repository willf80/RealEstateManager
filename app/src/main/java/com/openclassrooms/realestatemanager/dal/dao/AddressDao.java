package com.openclassrooms.realestatemanager.dal.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.AddressDisplayedInfo;
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

    @Query("DELETE FROM AddressProperties WHERE propertyId = :propertyId")
    public abstract void deleteAddressProperties(long propertyId);

    @Transaction
    @Query("SELECT * FROM AddressProperties WHERE propertyId = :propertyId")
    public abstract LiveData<AddressDisplayedInfo> getAddressWithPropertyId(long propertyId);

    // Content provider
    @Query("SELECT * FROM AddressProperties WHERE propertyId = :propertyId")
    public abstract Cursor getAddressWithCursor(long propertyId);
}
