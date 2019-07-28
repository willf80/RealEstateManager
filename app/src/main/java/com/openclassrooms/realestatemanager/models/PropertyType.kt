package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PropertyType {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var label: String? = null
}
