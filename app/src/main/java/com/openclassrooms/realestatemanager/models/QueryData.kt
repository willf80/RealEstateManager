package com.openclassrooms.realestatemanager.models

class QueryData{
    var minValue: Long = 0
    var maxValue: Long = 0

    constructor(minValue: Long){
        this.minValue = minValue
    }

    constructor(minValue: Long, maxValue: Long){
        this.minValue = minValue
        this.maxValue = maxValue
    }
}