package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.utils.DateConverters

import java.util.Date

@Entity(indices = [Index("propertyTypeId"), Index("userId")],
        foreignKeys = [
            ForeignKey(entity = PropertyType::class, parentColumns = ["id"], childColumns = ["propertyTypeId"]),
            ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
        ])
class Property {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var price: Double = 0.toDouble()
    var area: Double = 0.toDouble()
    var numberOfRooms: Int = 0
    var numberOfBathrooms: Int = 0
    var numberOfBedrooms: Int = 0
    var description: String? = null
    var addressLine2: String? = null
    var isSold: Boolean = false
    var createdDate: Date? = null
    var modifiedDate: Date? = null
    var soldDate: Date? = null
    var propertyTypeId: Long = 0 //FK
    var userId: Long = 0 //FK

    fun formContentValues(values: ContentValues) : Property {
        val property = Property()

        if(values.containsKey("id")) property.id = values.getAsLong("id")
        if(values.containsKey("price")) property.price = values.getAsDouble("price")
        if(values.containsKey("area")) property.area = values.getAsDouble("area")
        if(values.containsKey("numberOfRooms")) property.numberOfRooms = values.getAsInteger("numberOfRooms")
        if(values.containsKey("numberOfBathrooms")) property.numberOfBathrooms = values.getAsInteger("numberOfBathrooms")
        if(values.containsKey("numberOfBedrooms")) property.numberOfBedrooms = values.getAsInteger("numberOfBedrooms")
        if(values.containsKey("description")) property.description = values.getAsString("description")
        if(values.containsKey("addressLine2")) property.addressLine2 = values.getAsString("addressLine2")
        if(values.containsKey("isSold")) property.isSold = values.getAsBoolean("isSold")
        if(values.containsKey("createdDate")) property.createdDate = DateConverters.fromTimestamp(values.getAsLong("createdDate"))
        if(values.containsKey("modifiedDate")) property.modifiedDate = DateConverters.fromTimestamp(values.getAsLong("modifiedDate"))
        if(values.containsKey("soldDate")) property.soldDate = DateConverters.fromTimestamp(values.getAsLong("soldDate"))
        if(values.containsKey("propertyTypeId")) property.propertyTypeId = values.getAsLong("propertyTypeId")
        if(values.containsKey("userId")) property.userId = values.getAsLong("userId")

        return property
    }
}
