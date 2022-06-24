package com.lx.travelprevention.ui.agency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.model.TravelPreventionRepository
import com.lx.travelprevention.model.entity.AgencyEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgencyViewModel @Inject constructor(
    private val repository: TravelPreventionRepository
) : ViewModel() {

    private val _agency = MutableStateFlow<StateResult<AgencyEntity>>(StateResult.Loading)
    val agency = _agency.asStateFlow()

    fun loadAgency(cityId: String) = viewModelScope.launch {
        if (cityId.isEmpty()) {
            _agency.value = StateResult.Success(data = null)
        } else {
            repository.nucleicAcidAgency(cityId = cityId).onStart {
                _agency.value = StateResult.Loading
            }.catch {
                _agency.value = StateResult.Error(it)
            }.collectLatest {
                _agency.value = it
            }
        }
    }
}