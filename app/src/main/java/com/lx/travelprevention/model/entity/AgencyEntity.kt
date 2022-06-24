package com.lx.travelprevention.model.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AgencyEntity(
    @SerializedName("city")
    val city: String,
    @SerializedName("city_id")
    val cityId: String,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("province")
    val province: String
) {
    @Keep
    data class Data(
        @SerializedName("address")
        val address: String,
        @SerializedName("city")
        val city: String,
        @SerializedName("city_id")
        val cityId: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("province")
        val province: String
    )
}