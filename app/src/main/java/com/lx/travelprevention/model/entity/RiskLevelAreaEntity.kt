package com.lx.travelprevention.model.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * 风险等级地区查询
 *
 * @property highCount 高风险地区数量
 * @property highList 高风险地区列表
 * @property middleCount 中风险地区数量
 * @property middleList 中风险地区列表
 * @property updatedDate 更新时间
 * @constructor Create empty Risk level area entity
 */
@Keep
data class RiskLevelAreaEntity(
    @SerializedName("high_count")
    val highCount: String,
    @SerializedName("high_list")
    val highList: List<Area>,
    @SerializedName("middle_count")
    val middleCount: String,
    @SerializedName("middle_list")
    val middleList: List<Area>,
    @SerializedName("updated_date")
    val updatedDate: String
) {
    @Keep
    data class Area(
        @SerializedName("area_name")
        val areaName: String,
        @SerializedName("city")
        val city: String,
        @SerializedName("communitys")
        val communitys: List<String>,
        @SerializedName("county")
        val county: String,
        @SerializedName("county_code")
        val countyCode: String,
        @SerializedName("province")
        val province: String,
        @SerializedName("type")
        val type: String
    )
}