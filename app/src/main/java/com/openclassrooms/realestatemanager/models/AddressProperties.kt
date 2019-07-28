package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(indices = [Index("propertyId"), Index("addressId")],
        primaryKeys = ["propertyId", "addressId"],
        foreignKeys =
            [ForeignKey(entity = Property::class, parentColumns = ["id"], childColumns = ["propertyId"]),
            ForeignKey(entity = Address::class, parentColumns = ["id"], childColumns = ["addressId"])])
class AddressProperties(var propertyId: Long // PK|FK
                        , var addressId: Long // PK|FK
)
