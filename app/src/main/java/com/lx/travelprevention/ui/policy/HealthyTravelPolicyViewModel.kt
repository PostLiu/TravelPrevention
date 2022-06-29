package com.lx.travelprevention.ui.policy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.model.TravelPreventionRepository
import com.lx.travelprevention.model.entity.HealthyTravelPolicyEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthyTravelPolicyViewModel @Inject constructor(
    private val repository: TravelPreventionRepository
) : ViewModel() {

    private val _healthyTravel =
        MutableStateFlow<StateResult<HealthyTravelPolicyEntity>>(StateResult.Success(data = null))
    val healthyTravel = _healthyTravel.asStateFlow()

    fun healthyTravelPolicy(from: String, to: String) = viewModelScope.launch {
        repository.healthyTravelPolicy(from, to).catch {
            _healthyTravel.emit(StateResult.Error(it))
        }.collectLatest {
            _healthyTravel.emit(it)
        }
    }
}