@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lx.travelprevention.model

import com.lx.travelprevention.common.DataResult.Companion.isSuccess
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.dao.CityDao
import com.lx.travelprevention.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelPreventionRepository @Inject constructor(
    private val apiService: ApiService, private val dao: CityDao
) {

    // 获取城市
    fun cities() = dao.allCities().flatMapLatest {
        if (it.isEmpty()) {
            citiesFromNetwork()
        } else {
            dao.allCities()
        }
    }.flowOn(Dispatchers.IO)

    // 网络获取城市
    private fun citiesFromNetwork() = flow {
        val result = apiService.cities()
        val cities = if (result.isSuccess) {
            result.data?.flatMap { parent ->
                parent.citys.map { it.copy(parent = parent.province) }
            }
        } else {
            emptyList()
        }.orEmpty()
        emit(cities)
    }

    // 指定城市的核酸机构
    fun nucleicAcidAgency(cityId: String) = flow {
        val result = apiService.nucleicAcidAgency(cityId = cityId)
        if (result.isSuccess) {
            emit(StateResult.Success(result.data))
        } else {
            emit(StateResult.Failed(result.message))
        }
    }.flowOn(Dispatchers.IO)

    // 风险等级地区查询
    fun riskLevelArea() = flow {
        val result = apiService.riskLevelArea()
        if (result.isSuccess) {
            emit(StateResult.Success(result.data))
        } else {
            emit(StateResult.Failed(result.message))
        }
    }.flowOn(Dispatchers.IO)

    // 查询健康出行政策（从哪来，到哪去）
    fun healthyTravelPolicy(from: String, to: String) = flow {
        val result = apiService.healthyTravelPolicy(from = from, to = to)
        if (result.isSuccess) {
            emit(StateResult.Success(result.data))
        } else {
            emit(StateResult.Failed(result.message))
        }
    }.flowOn(Dispatchers.IO)

    // 获取城市天气
    fun weatherCity(city: String) = flow {
        val result = apiService.weatherCity(city = city)
        if (result.isSuccess) {
            emit(StateResult.Success(result.data))
        } else {
            emit(StateResult.Failed(result.message))
        }
    }.flowOn(Dispatchers.IO)
}