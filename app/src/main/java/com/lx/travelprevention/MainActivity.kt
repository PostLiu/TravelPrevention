@file:OptIn(ExperimentalMaterialApi::class)

package com.lx.travelprevention

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lx.travelprevention.common.Routes
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.ui.agency.AgencyPage
import com.lx.travelprevention.ui.agency.AgencyViewModel
import com.lx.travelprevention.ui.area.RiskLevelAreaPage
import com.lx.travelprevention.ui.area.RiskLevelAreaViewModel
import com.lx.travelprevention.ui.city.CityPage
import com.lx.travelprevention.ui.city.CityViewModel
import com.lx.travelprevention.ui.policy.HealthyTravelPolicyPage
import com.lx.travelprevention.ui.policy.HealthyTravelPolicyViewModel
import com.lx.travelprevention.ui.theme.TravelPreventionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelPreventionTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = Routes.Home) {
                        // 主页
                        composable(route = Routes.Home) {
                            HomePage(navController = navController)
                        }
                        // 城市
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
                        // 核酸机构
                        composable(route = Routes.Agency) {
                            val viewModel: AgencyViewModel = hiltViewModel()
                            Log.w(TAG, "onCreate: ${it.lifecycle.currentState}")
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
                        // 风险地区
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
                        // 健康出行政策
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
                                Log.e(TAG, "onCreate: Healthy=====>$fromCityId-----$toCityId")
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
                }
            }
        }
    }
}

@Composable
fun HomePage(navController: NavController = rememberNavController()) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "防疫出行") })
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                TravelPreventionTitle()
            }
            item {
                NucleicAcidAgency() {
                    navController.navigate(Routes.Agency)
                }
            }
            item {
                RiskLevelArea() {
                    navController.navigate(Routes.Area)
                }
            }
            item {
                HealthyTravelPolicy() {
                    navController.navigate(Routes.Policy)
                }
            }
        }
    }
}

// 1、政策整理自当地政府等公开发布的消息，如有更新或错漏，请以最新政策为准，建议在出行前咨询当地防疫部门、机场、火车站等。
// 2、本查询服务主要提供地市级（州）有关部门发布的政策信息，暂不覆盖县级官方（县、县级市、地级市辖区等）信息。
// 3、免费用户接口每天免费使用50次，等项目研发上线后，可与聚合官方联系沟通，获得更多的免费次数。
@Composable
fun TravelPreventionTitle() {
    Text(text = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("1、")
        }
        withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
            append("政策整理自当地政府等公开发布的消息，如有更新或错漏，请以最新政策为准，建议在出行前咨询当地防疫部门、机场、火车站等。")
        }
        append("\n")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("2、")
        }
        withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
            append("本查询服务主要提供地市级（州）有关部门发布的政策信息，暂不覆盖县级官方（县、县级市、地级市辖区等）信息。")
        }
    }, modifier = Modifier.padding(6.dp))
}

// 核酸机构
@Composable
fun NucleicAcidAgency(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12),
        border = BorderStroke(
            2.dp, brush = Brush.linearGradient(
                colors = listOf(
                    Color.Red, Color.Yellow, Color.Blue, Color.Green,
                )
            )
        ),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_nucleic_acid_agency),
                    tint = Color.Blue,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(text = "查询核酸检测机构")
            }
            Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

// 风险等级地区
@Composable
fun RiskLevelArea(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12),
        border = BorderStroke(
            2.dp, brush = Brush.linearGradient(
                colors = listOf(
                    Color.Blue, Color.Green, Color.Red, Color.Yellow,
                )
            )
        ),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_risk_level_area),
                    tint = Color.Red,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(text = "查询风险等级地区")
            }
            Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

// 健康出行政策
@Composable
fun HealthyTravelPolicy(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12),
        border = BorderStroke(
            2.dp, brush = Brush.linearGradient(
                colors = listOf(
                    Color.Green, Color.Red, Color.Yellow, Color.Blue,
                )
            )
        ),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_healthy_travel_policy),
                    tint = Color.Green,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(text = "查询健康出行政策")
            }
            Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}