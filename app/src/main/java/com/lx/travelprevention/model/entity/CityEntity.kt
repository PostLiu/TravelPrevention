package com.lx.travelprevention.model.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
data class CityEntity(
    @SerializedName("citys")
    val citys: List<City>,
    @SerializedName("province")
    val province: String,
    @SerializedName("province_id")
    val provinceId: String
)

@Keep
@Entity(tableName = "city")
data class City(
    @SerializedName("city")
    val city: String,
    @PrimaryKey
    @SerializedName("city_id")
    val cityId: String,
    @SerializedName("province")
    val parent: String
)