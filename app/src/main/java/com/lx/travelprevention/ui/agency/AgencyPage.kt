@file:OptIn(ExperimentalFoundationApi::class)

package com.lx.travelprevention.ui.agency

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lx.travelprevention.common.Routes
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.composable.*
import com.lx.travelprevention.model.entity.AgencyEntity

@Composable
fun AgencyPage(
    navController: NavController = rememberNavController(), agency: StateResult<AgencyEntity>
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "核酸机构") }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack, contentDescription = null
                )
            }
        })
    }) { paddingValues ->
        when (agency) {
            is StateResult.Loading -> {
                LoadingLayout(paddingValues = paddingValues)
            }
            is StateResult.Error -> {
                ErrorLayout(paddingValues = paddingValues, throwable = agency.throwable)
            }
            is StateResult.Failed -> {
                FailedLayout(paddingValues = paddingValues, failedMessage = agency.message)
            }
            is StateResult.Success -> {
                if (agency.data == null) {
                    EmptyLayout(
                        paddingValues = paddingValues,
                        contentAlignment = Alignment.TopCenter
                    ) {
                        ChooseCity() {
                            navController.navigate(Routes.City)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        stickyHeader {
                            ChooseCity(cityName = agency.data.city) {
                                navController.navigate(Routes.City)
                            }
                        }

                        items(agency.data.data) {
                            AgencyItem(data = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AgencyItem(data: AgencyEntity.Data) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Yellow, Color.Green)),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White)
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Text(text = buildAnnotatedString {
            withStyle(SpanStyle().copy(fontSize = 16.sp, fontWeight = FontWeight.Bold)) {
                append(data.name)
            }
            append("\n")
            withStyle(
                SpanStyle().copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.typography.h2.color
                )
            ) {
                append("区域：")
                append(data.province)
                append(data.city)
                append("\n")
                append("联系电话：")
                append(data.phone)
                append("\n")
                append("详细地址：")
                append(data.address)
            }
        })
    }
}

@Composable
fun ChooseCity(cityName: String = "请选择城市", choose: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(0.dp))
        .border(
            width = 2.dp,
            brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Yellow, Color.Green)),
            shape = RoundedCornerShape(0.dp)
        )
        .background(Color.White)
        .clickable { choose() }
        .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = cityName, style = MaterialTheme.typography.subtitle2)
        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null)
    }
}