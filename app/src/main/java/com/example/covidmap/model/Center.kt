package com.example.covidmap.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.covidmap.constant.COVIDConstants.CENTER_DB
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = CENTER_DB)
data class Center(
    val address: String,
    val centerName: String,
    val centerType: String,
    val createdAt: String,
    val facilityName: String,
    @PrimaryKey
    val id: Int,
    val lat: String,
    val lng: String,
    val org: String,
    val phoneNumber: String,
    val sido: String,
    val sigungu: String,
    val updatedAt: String,
    val zipCode: String
):Serializable