package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("propertyId")],
        foreignKeys = [ForeignKey(entity = Property::class,
                parentColumns = ["id"],
                childColumns = ["propertyId"])])
class Media {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var label: String? = null
    var fileName: String? = null
    var isCover: Boolean = false

    var propertyId: Long = 0 // FK
}
