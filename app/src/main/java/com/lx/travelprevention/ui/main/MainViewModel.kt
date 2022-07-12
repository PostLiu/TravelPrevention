package com.lx.travelprevention.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.model.TravelPreventionRepository
import com.lx.travelprevention.model.entity.City
import com.lx.travelprevention.model.entity.CityEntity
import com.lx.travelprevention.model.entity.CityWeatherEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TravelPreventionRepository, application: Application
) : AndroidViewModel(application) {

    private val mCityList = MutableStateFlow<StateResult<List<City>>>(StateResult.Loading)
    val cityList = mCityList.asStateFlow()

    init {
        loadCities()
    }

    private fun loadCities() = viewModelScope.launch {
        repository.cities().onStart {
            mCityList.emit(StateResult.Loading)
        }.catch {
            mCityList.emit(StateResult.Error(it))
        }.collectLatest { result ->
            if (result.isEmpty()) {
                val json = getApplication<Application>().assets.open("city.json").use { input ->
                    val stringBuilder = StringBuilder()
                    val bufferedReader = BufferedReader(InputStreamReader(input))
                    var line: String?
                    while (bufferedReader.readLine().also { line = it } != null) {
                        stringBuilder.append(line)
                    }
                    stringBuilder.toString()
                }
                val cities = Gson().fromJson<List<CityEntity>>(
                    json, object : TypeToken<List<CityEntity>>() {}.type
                ).orEmpty()
                val dataList = cities.flatMap { parent ->
                    parent.citys.map { it.copy(parent = parent.province) }
                }
                mCityList.emit(StateResult.Success(data = dataList))
            } else {
                mCityList.emit(StateResult.Success(result))
            }
        }
    }

    private val mWeather = MutableStateFlow<StateResult<CityWeatherEntity>>(StateResult.Loading)
    val weather = mWeather.asStateFlow()

    fun weatherCity(city: String) = viewModelScope.launch {
        repository.weatherCity(city = city).catch {
            mWeather.emit(StateResult.Error(it))
        }.collectLatest {
            mWeather.emit(it)
        }
    }
}