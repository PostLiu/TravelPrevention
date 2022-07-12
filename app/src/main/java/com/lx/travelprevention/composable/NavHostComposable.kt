package com.lx.travelprevention.composable

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lx.travelprevention.MainActivity
import com.lx.travelprevention.common.Constant
import com.lx.travelprevention.common.DataStoreUtils
import com.lx.travelprevention.common.Routes
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.model.entity.ThemeType
import com.lx.travelprevention.ui.agency.AgencyPage
import com.lx.travelprevention.ui.agency.AgencyViewModel
import com.lx.travelprevention.ui.area.RiskLevelAreaPage
import com.lx.travelprevention.ui.area.RiskLevelAreaViewModel
import com.lx.travelprevention.ui.city.CityPage
import com.lx.travelprevention.ui.city.CityViewModel
import com.lx.travelprevention.ui.main.HomePage
import com.lx.travelprevention.ui.main.MainViewModel
import com.lx.travelprevention.ui.policy.HealthyTravelPolicyPage
import com.lx.travelprevention.ui.policy.HealthyTravelPolicyViewModel
import com.lx.travelprevention.ui.set.theme.MultipleThemesPage


fun NavGraphBuilder.theme(
    navController: NavHostController,
    selectedTheme: ThemeType,
    onSelected: ((ThemeType) -> Unit)? = null
) {
    composable(route = Routes.Setting) {
        MultipleThemesPage(
            navController = navController,
            themeType = selectedTheme
        ) {
            onSelected?.invoke(it)
        }
    }
}

fun NavGraphBuilder.policy(navController: NavHostController) {
    composable(route = Routes.Policy) {
        val viewModel: HealthyTravelPolicyViewModel = hiltViewModel()
        // 获取出发地回传的参数数据
        val from by it.savedStateHandle.getLiveData("from", "").observeAsState()
        val fromCityId =
            if (from.isNullOrEmpty()) "" else from.orEmpty().split("|")[0]
        val fromCityName =
            if (from.isNullOrEmpty()) "" else from.orEmpty().split("|")[1]
        // 获取目的地回传的参数数据
        val to by it.savedStateHandle.getLiveData("to", "").observeAsState()
        val toCityId =
            if (to.isNullOrEmpty()) "" else to.orEmpty().split("|")[0]
        val toCityName =
            if (to.isNullOrEmpty()) "" else to.orEmpty().split("|")[1]

        if (fromCityId.isNotEmpty() && toCityId.isNotEmpty()) {
            Log.e(MainActivity.TAG, "onCreate: Healthy=====>$fromCityId-----$toCityId")
            LaunchedEffect(key1 = Unit, block = {
                viewModel.healthyTravelPolicy(fromCityId, toCityId)
            })
        }
        val healthyTravel by viewModel.healthyTravel.collectAsState(
            initial = if (from.isNullOrEmpty() && to.isNullOrEmpty()) StateResult.Success(
                data = null
            ) else StateResult.Loading
        )
        HealthyTravelPolicyPage(
            navController = navController,
            healthy = healthyTravel,
            fromCityName = fromCityName,
            toCityName = toCityName
        )
    }
}

fun NavGraphBuilder.area(navController: NavHostController) {
    composable(route = Routes.Area) {
        val viewModel: RiskLevelAreaViewModel = hiltViewModel()
        // 请求数据
        LaunchedEffect(key1 = Unit, block = {
            viewModel.riskLevelArea()
        })
        val riskLevelArea by viewModel.riskLevelArea.collectAsState()
        RiskLevelAreaPage(
            navController = navController, levelArea = riskLevelArea
        )
    }
}

fun NavGraphBuilder.agency(navController: NavHostController) {
    composable(route = Routes.Agency) {
        val viewModel: AgencyViewModel = hiltViewModel()
        Log.w(MainActivity.TAG, "onCreate: ${it.lifecycle.currentState}")
        // 获取回传的城市id
        val returnCityId by it.savedStateHandle.getLiveData("city_id", "")
            .observeAsState()
        LaunchedEffect(key1 = Unit, block = {
            Log.w("TAG", "onCreate: 获取选择的城市ID is $returnCityId")
            viewModel.loadAgency(cityId = returnCityId.orEmpty())
        })
        val agency by viewModel.agency.collectAsState(
            initial = if (returnCityId.isNullOrEmpty()) StateResult.Success(
                data = null
            ) else StateResult.Loading
        )
        AgencyPage(
            navController = navController, agency = agency
        )
    }
}

fun NavGraphBuilder.city(navController: NavHostController) {
    composable(
        route = Routes.City.plus("/{type}"),
        arguments = listOf(navArgument("type") { type = NavType.StringType })
    ) {
        val type = it.arguments?.getString("type").orEmpty()
        val viewModel: CityViewModel = hiltViewModel()
        val cities = viewModel.cities.collectAsState().value
        CityPage(navController = navController,
            cities = cities,
            type = type,
            retry = { viewModel.loadCities() })
    }
}

fun NavGraphBuilder.main(navController: NavHostController, dataStoreUtils: DataStoreUtils) {
    composable(route = Routes.Home) {
        var selectedCity by remember { mutableStateOf("") }
        // 读取本地缓存的城市名称
        LaunchedEffect(key1 = Unit, block = {
            selectedCity = dataStoreUtils.readString(Constant.CITY_NAME) ?: ""
        })
        // 保存选择的城市到本地缓存
        LaunchedEffect(key1 = selectedCity, block = {
            dataStoreUtils.writeString(Constant.CITY_NAME, selectedCity)
        })
        val viewModel: MainViewModel = hiltViewModel()
        val cityList = viewModel.cityList.collectAsState().value
        val weather = viewModel.weather.collectAsState().value
        if (selectedCity.isNotEmpty()) {
            Log.e(MainActivity.TAG, "main: 选择的城市：$selectedCity")
            LaunchedEffect(key1 = selectedCity, block = {
                viewModel.weatherCity(selectedCity)
            })
        }
        HomePage(
            navController = navController,
            cityList = if (cityList is StateResult.Success) cityList.data.orEmpty() else emptyList(),
            weather = if (weather is StateResult.Success) weather.data else null,
        ) {
            selectedCity = it
        }
    }
}