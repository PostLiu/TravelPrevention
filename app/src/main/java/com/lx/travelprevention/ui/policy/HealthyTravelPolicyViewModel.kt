package com.lx.travelprevention.ui.policy

import androidx.lifecycle.ViewModel
import com.lx.travelprevention.model.TravelPreventionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HealthyTravelPolicyViewModel @Inject constructor(
    private val repository: TravelPreventionRepository
) : ViewModel() {
}