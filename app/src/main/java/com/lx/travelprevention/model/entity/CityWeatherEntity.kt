package com.lx.travelprevention.model.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * City weather entity
 *
 * @property city       查询的城市名称
 * @property future     未来几天天气情况
 * @property realtime   当前天气详情
 * @constructor Create empty City weather entity
 */
@Keep
data class CityWeatherEntity(
    @SerializedName("city")
    val city: String,
    @SerializedName("future")
    val future: List<Future>,
    @SerializedName("realtime")
    val realtime: Realtime
) {
    @Keep
    data class Future(
        @SerializedName("date")
        val date: String,
        @SerializedName("direct")
        val direct: String,
        @SerializedName("temperature")
        val temperature: String,
        @SerializedName("weather")
        val weather: String,
        @SerializedName("wid")
        val wid: Wid
    ) {
        @Keep
        data class Wid(
            @SerializedName("day")
            val day: String,
            @SerializedName("night")
            val night: String
        )
    }

    @Keep
    data class Realtime(
        @SerializedName("aqi")
        val aqi: String,
        @SerializedName("direct")
        val direct: String,
        @SerializedName("humidity")
        val humidity: String,
        @SerializedName("info")
        val info: String,
        @SerializedName("power")
        val power: String,
        @SerializedName("temperature")
        val temperature: String,
        @SerializedName("wid")
        val wid: String
    )
}