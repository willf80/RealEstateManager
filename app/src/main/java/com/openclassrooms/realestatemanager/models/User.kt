package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var fullName: String? = null
    var email: String? = null

}
