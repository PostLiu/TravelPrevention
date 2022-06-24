package com.lx.travelprevention.ui.city

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.model.TravelPreventionRepository
import com.lx.travelprevention.model.entity.City
import com.lx.travelprevention.model.entity.CityEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val repository: TravelPreventionRepository, application: Application
) : AndroidViewModel(application) {

    private val _cities = MutableStateFlow<StateResult<List<City>>>(StateResult.Loading)
    val cities = _cities.asStateFlow()

    init {
        loadCities()
    }

    fun loadCities() = viewModelScope.launch {
        repository.cities().onStart {
            _cities.emit(StateResult.Loading)
        }.catch {
            _cities.emit(StateResult.Error(it))
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
                _cities.emit(StateResult.Success(data = dataList))
            } else {
                _cities.emit(StateResult.Success(result))
            }
        }
    }
}