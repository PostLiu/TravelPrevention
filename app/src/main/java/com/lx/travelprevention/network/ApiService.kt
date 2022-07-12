package com.lx.travelprevention.network

import com.lx.travelprevention.common.Constant
import com.lx.travelprevention.common.DataResult
import com.lx.travelprevention.model.entity.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // 获取城市清单
    @GET("springTravel/citys")
    suspend fun cities(
        @Query("key") key: String = Constant.KEY
    ): DataResult<List<CityEntity>>

    // 获取指定城市的核酸检测机构
    @FormUrlEncoded
    @POST("springTravel/hsjg")
    suspend fun nucleicAcidAgency(
        @Field("city_id") cityId: String,
        @Field("key") key: String = Constant.KEY
    ): DataResult<AgencyEntity>

    // 查询出行防疫政策
    @FormUrlEncoded
    @POST("springTravel/query")
    suspend fun healthyTravelPolicy(
        @Field("from") from: String,
        @Field("to") to: String,
        @Field("key") key: String = Constant.KEY
    ): DataResult<HealthyTravelPolicyEntity>

    // 查询风险疫情地区
    @GET("springTravel/risk")
    suspend fun riskLevelArea(
        @Query("key") key: String = Constant.KEY
    ): DataResult<RiskLevelAreaEntity>

    // 查询指定城市的天气
    @FormUrlEncoded
    @POST("simpleWeather/query")
    suspend fun weatherCity(
        @Field("city") city: String,
        @Field("key") key: String = Constant.WEATHER_KEY
    ): DataResult<CityWeatherEntity>
}