package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Relation

class AddressDisplayedInfo {
    @Embedded
    var addressProperties: AddressProperties? = null

    @Relation(parentColumn = "addressId", entityColumn = "id")
    var address: List<Address> = ArrayList()
}