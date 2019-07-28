package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Address {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var addressLine1: String? = null

    var postalCode: String? = null
}
