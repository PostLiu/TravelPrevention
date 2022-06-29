package com.lx.travelprevention.ui.policy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.lx.travelprevention.common.Routes
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.composable.EmptyLayout
import com.lx.travelprevention.composable.ErrorLayout
import com.lx.travelprevention.composable.FailedLayout
import com.lx.travelprevention.composable.LoadingLayout
import com.lx.travelprevention.model.entity.HealthyTravelPolicyEntity
import com.lx.travelprevention.ui.theme.TravelPreventionTheme

@Composable
fun HealthyTravelPolicyPage(
    navController: NavController = rememberNavController(),
    healthy: StateResult<HealthyTravelPolicyEntity> = StateResult.Loading,
    fromCityName: String = "",
    toCityName: String = ""
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "健康出行政策") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->
        when (healthy) {
            is StateResult.Loading -> {
                LoadingLayout(paddingValues = paddingValues)
            }
            is StateResult.Error -> {
                ErrorLayout(paddingValues = paddingValues, throwable = healthy.throwable)
            }
            is StateResult.Failed -> {
                FailedLayout(paddingValues = paddingValues, failedMessage = healthy.message)
            }
            is StateResult.Success -> {
                if (healthy.data == null) {
                    EmptyLayout(
                        paddingValues = paddingValues, contentAlignment = Alignment.TopCenter
                    ) {
                        FromAndToCity(
                            fromCity = fromCityName.ifEmpty { "选择出发地" },
                            toCity = toCityName.ifEmpty { "选择目的地" },
                            fromClick = {
                                navController.navigate(Routes.City.plus("/from"))
                            },
                            toClick = {
                                navController.navigate(Routes.City.plus("/to"))
                            },
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = paddingValues)
                    ) {
                        item {
                            FromAndToCity(fromCity = healthy.data.fromInfo.cityName,
                                toCity = healthy.data.toInfo.cityName,
                                fromClick = {
                                    navController.navigate(Routes.City.plus("/from"))
                                },
                                toClick = {
                                    navController.navigate(Routes.City.plus("/to"))
                                })
                        }
                        item {
                            HealthyTravelPolicyItem(data = healthy.data.fromInfo)
                        }
                        item {
                            HealthyTravelPolicyItem(data = healthy.data.toInfo)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FromAndToCity(fromCity: String, toCity: String, fromClick: () -> Unit, toClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        FromOrToCity(name = fromCity) {
            fromClick()
        }
        FromOrToCity(name = toCity) {
            toClick()
        }
    }
}

@Composable
fun HealthyTravelPolicyItem(data: HealthyTravelPolicyEntity.Info) {
    val level = when (data.riskLevel) {
        "0" -> "暂无"
        "1" -> "低风险"
        "2" -> "中风险"
        "3" -> "高风险"
        "4" -> "部分地区中风险"
        "5" -> "部分地区高风险"
        "6" -> "部分地区中、高风险"
        else -> "未知"
    }
    val levelColor = when (data.riskLevel) {
        "1" -> Color.Green
        "2", "4" -> Color.Yellow
        "3", "5", "6" -> Color.Red
        else -> Color.LightGray
    }
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp, brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Red, Color.Yellow, Color.Green
                    ).shuffled()
                ), shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White)
            .padding(12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.LocationOn, contentDescription = null, tint = Color.Blue
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = data.cityName)
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = level,
                fontSize = 12.sp,
                color = levelColor,
                modifier = Modifier.align(Alignment.Bottom)
            )
        }
        Divider(modifier = Modifier.padding(vertical = 6.dp))
        Text(text = buildAnnotatedString {
            withStyle(SpanStyle().copy(fontWeight = FontWeight.Bold)) {
                append("高风险地区，进入政策描述")
            }
            append("\n")
            withStyle(SpanStyle()) {
                append(data.highInDesc.ifEmpty { "暂无" })
            }
            append("\n")
            withStyle(SpanStyle().copy(fontWeight = FontWeight.Bold)) {
                append("低风险地区，进入政策描述")
            }
            append("\n")
            withStyle(SpanStyle()) {
                append(data.lowInDesc.ifEmpty { "暂无" })
            }
            append("\n")
            withStyle(SpanStyle().copy(fontWeight = FontWeight.Bold)) {
                append("出行政策描述")
            }
            append("\n")
            withStyle(SpanStyle()) {
                append(data.outDesc.ifEmpty { "暂无" })
            }
        })
    }
}

@Composable
fun RowScope.FromOrToCity(name: String = "", click: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp, brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Red, Color.Yellow, Color.Green
                    ).shuffled()
                ), shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White)
            .padding(12.dp)
            .weight(1f)
            .clickable { click() },
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name)
        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null)
    }
}

@Preview
@Composable
fun HealthyTravelPolicyPreview() {
    TravelPreventionTheme {
        val json =
            "{\n" + "\"province_id\": \"31\",\n" + "\"city_id\": \"10349\",\n" + "\"city_name\": \"温州\",\n" + "\"health_code_desc\": \"支付宝扫一扫，进入小程序\",\n" + "\"health_code_gid\": \"https://www.sdk.cn/details/vjxQ9bLPEp70bqnKOZ\",\n" + "\"health_code_name\": \"浙江健康码\",\n" + "\"health_code_picture\": \"https://cdn.juhekeji.com/spring_travel/ad9ef3f794107b400aed52e0e721b578.png\",\n" + "\"health_code_style\": \"1\",\n" + "\"high_in_desc\": \"1.疫情高风险地区，经综合评估，可对其所在城市来浙返浙人员实行“7+7”健康管理措施，即先实施7天居家健康观察，第1天和第7天分别进行一次核酸检测；对居家健康观察期满核酸检测阴性者，继续实施7天日常健康监测，期满再进行一次核酸检测。\\n2.疫情中风险地区，所在街道或县（市、区）来浙返浙人员，一律核验3天内核酸检测阴性证明；对无法提供相关证明的，立即引导到指定场所接受核酸检测；对结果为阴性者实施7天日常健康监测，期满进行一次核酸检测。\\n3.入境人员需14天集中隔离+7天居家健康观察+7天健康监测。\",\n" + "\"low_in_desc\": \"低风险地区进温州无需持核酸阴性证明及隔离，需持健康码绿码。\",\n" + "\"out_desc\": \"出行无需核酸阴性证明，非必要避免前往中高风险地区。\",\n" + "\"province_name\": \"浙江\",\n" + "\"risk_level\": \"1\"\n" + "}"
        val data = Gson().fromJson(json, HealthyTravelPolicyEntity.Info::class.java)
        HealthyTravelPolicyItem(data = data)
    }
}