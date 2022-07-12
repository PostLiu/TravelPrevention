@file:OptIn(ExperimentalMaterialApi::class)

package com.lx.travelprevention.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lx.travelprevention.R
import com.lx.travelprevention.common.Routes
import com.lx.travelprevention.model.entity.City
import com.lx.travelprevention.model.entity.CityWeatherEntity

@Composable
fun HomePage(
    navController: NavController = rememberNavController(),
    cityList: List<City> = emptyList(),
    weather: CityWeatherEntity? = null,
    onSelected: (city: String) -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "防疫出行") }, actions = {
            IconButton(onClick = {
                navController.navigate(Routes.Setting)
            }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = null)
            }
        })
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CityWeather(
                    cityList = cityList,
                    weather = weather
                ) {
                    onSelected.invoke(it)
                }
            }
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
private fun TravelPreventionTitle() {
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
private fun NucleicAcidAgency(onClick: () -> Unit) {
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
private fun RiskLevelArea(onClick: () -> Unit) {
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
private fun HealthyTravelPolicy(onClick: () -> Unit) {
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

// 天气
@Composable
private fun CityWeather(
    cityList: List<City>,
    weather: CityWeatherEntity? = null,
    onSelected: (city: String) -> Unit
) {
    var showCityPopup by remember { mutableStateOf(false) }
    var selectCity by remember { mutableStateOf("请选择城市") }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clickable {
                    showCityPopup = true
                },
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = buildAnnotatedString {
                withStyle(
                    SpanStyle().copy(
                        color = MaterialTheme.colors.primary,
                        fontSize = 28.sp
                    )
                ) {
                    append(weather?.city.orEmpty().ifEmpty { selectCity })
                }
                if (weather?.realtime == null) {
                    withStyle(
                        SpanStyle().copy(
                            color = MaterialTheme.colors.primary,
                            fontSize = 14.sp
                        )
                    ) {
                        append("----")
                        append("天气数据为空")
                    }
                } else {
                    weather.realtime.also {
                        withStyle(
                            SpanStyle().copy(
                                color = MaterialTheme.colors.primary,
                                fontSize = 28.sp
                            )
                        ) {
                            append("----")
                            append(it.info)
                        }
                        withStyle(
                            SpanStyle().copy(
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.W900
                            )
                        ) {
                            append(it.temperature)
                            append("℃")
                        }
                    }
                }
            }, textAlign = TextAlign.Center)
        }
    }
    if (showCityPopup) {
        Popup(
            onDismissRequest = {
                showCityPopup = false
            }, alignment = Alignment.TopCenter,
            properties = PopupProperties(focusable = true),
            offset = IntOffset(x = 0, y = 100)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                items(cityList) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(12.dp)
                            .clickable {
                                selectCity = it.city
                                showCityPopup = false
                                onSelected.invoke(it.city)
                            },
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = it.city)
                    }
                }
            }
        }
    }
}