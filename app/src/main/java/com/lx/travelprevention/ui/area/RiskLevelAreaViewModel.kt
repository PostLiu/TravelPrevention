package com.lx.travelprevention.ui.area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.model.TravelPreventionRepository
import com.lx.travelprevention.model.entity.RiskLevelAreaEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RiskLevelAreaViewModel @Inject constructor(
    private val repository: TravelPreventionRepository
) : ViewModel() {

    private val _riskLevelArea =
        MutableStateFlow<StateResult<RiskLevelAreaEntity>>(StateResult.Loading)
    val riskLevelArea = _riskLevelArea.asStateFlow()

    fun riskLevelArea() = viewModelScope.launch {
        repository.riskLevelArea().catch {
            _riskLevelArea.value = StateResult.Error(it)
        }.collectLatest {
            _riskLevelArea.value = it
        }
    }
}