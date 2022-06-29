package com.lx.travelprevention.model.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class HealthyTravelPolicyEntity(
    @SerializedName("from_covid_info")
    val fromCovidInfo: CovidInfo,
    @SerializedName("from_info")
    val fromInfo: Info,
    @SerializedName("to_covid_info")
    val toCovidInfo: CovidInfo,
    @SerializedName("to_info")
    val toInfo: Info
) {

    @Keep
    data class Info(
        @SerializedName("city_id")
        val cityId: String,
        @SerializedName("city_name")
        val cityName: String,
        @SerializedName("health_code_desc")
        val healthCodeDesc: String,
        @SerializedName("health_code_gid")
        val healthCodeGid: String,
        @SerializedName("health_code_name")
        val healthCodeName: String,
        @SerializedName("health_code_picture")
        val healthCodePicture: String,
        @SerializedName("health_code_style")
        val healthCodeStyle: String,
        @SerializedName("high_in_desc")
        val highInDesc: String,
        @SerializedName("low_in_desc")
        val lowInDesc: String,
        @SerializedName("out_desc")
        val outDesc: String,
        @SerializedName("province_id")
        val provinceId: String,
        @SerializedName("province_name")
        val provinceName: String,
        @SerializedName("risk_level")
        val riskLevel: String
    )

    @Keep
    data class CovidInfo(
        @SerializedName("city_id")
        val cityId: String,
        @SerializedName("city_name")
        val cityName: String,
        @SerializedName("is_updated")
        val isUpdated: String,
        @SerializedName("today_confirm")
        val todayConfirm: String,
        @SerializedName("total_confirm")
        val totalConfirm: String,
        @SerializedName("total_dead")
        val totalDead: String,
        @SerializedName("total_heal")
        val totalHeal: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}