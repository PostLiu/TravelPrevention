package com.lx.travelprevention.ui.area

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Warning
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
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.composable.*
import com.lx.travelprevention.model.entity.RiskLevelAreaEntity


@Composable
fun RiskLevelAreaPage(
    navController: NavController = rememberNavController(),
    levelArea: StateResult<RiskLevelAreaEntity> = StateResult.Loading
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "风险等级地区") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->
        when (levelArea) {
            is StateResult.Loading -> {
                LoadingLayout(paddingValues = paddingValues)
            }
            is StateResult.Error -> {
                ErrorLayout(paddingValues = paddingValues,
                    throwable = levelArea.throwable,
                    retry = {})
            }
            is StateResult.Failed -> {
                FailedLayout(paddingValues = paddingValues, failedMessage = levelArea.message)
            }
            is StateResult.Success -> {
                if (levelArea.data == null) {
                    EmptyLayout(paddingValues = paddingValues) {

                    }
                } else {
                    SuccessLayout(paddingValues = paddingValues) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                LevelAreaCountItem(
                                    highCount = levelArea.data.highCount,
                                    middleCount = levelArea.data.middleCount,
                                    updatedDate = levelArea.data.updatedDate
                                )
                            }
                            if (levelArea.data.highList.isNotEmpty()) {
                                item {
                                    RiskLevelItemTitle(
                                        iconTint = MaterialTheme.colors.error,
                                        title = "高风险地区"
                                    )
                                }
                                items(levelArea.data.highList) {
                                    RiskLevelAreaItem(it)
                                }
                            }
                            if (levelArea.data.middleList.isNotEmpty()) {
                                item {
                                    RiskLevelItemTitle(
                                        iconTint = Color.Yellow,
                                        title = "中风险地区"
                                    )
                                }
                                items(levelArea.data.middleList) {
                                    RiskLevelAreaItem(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RiskLevelItemTitle(iconTint: Color, title: String) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Warning,
            contentDescription = null,
            tint = iconTint
        )
        Text(text = buildAnnotatedString {
            withStyle(
                SpanStyle().copy(
                    fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
            ) {
                append(title)
            }
        })
    }
}

@Composable
fun RiskLevelAreaItem(data: RiskLevelAreaEntity.Area) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Red,
                        Color.Yellow,
                        Color.Green
                    ).shuffled()
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White)
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Text(text = data.areaName, style = MaterialTheme.typography.body1)
        data.communitys.forEach { Text(text = it, style = MaterialTheme.typography.body2) }
    }
}

@Composable
fun LevelAreaCountItem(highCount: String, middleCount: String, updatedDate: String) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Red,
                        Color.Yellow,
                        Color.Green
                    ).shuffled()
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White)
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Text(text = buildAnnotatedString {
            withStyle(SpanStyle().copy(fontSize = 16.sp)) {
                append("更新时间：")
            }
            append("\n")
            withStyle(SpanStyle().copy(fontSize = 14.sp)) {
                append(updatedDate)
            }
        })
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = null,
                tint = MaterialTheme.colors.error
            )
            Text(text = buildAnnotatedString {
                withStyle(SpanStyle().copy(fontSize = 16.sp)) {
                    append("高风险地区：")
                }
                append("\u3000")
                withStyle(SpanStyle().copy(fontSize = 14.sp, color = MaterialTheme.colors.error)) {
                    append(highCount)
                    append("个")
                }
            })
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Rounded.Warning, contentDescription = null, tint = Color.Yellow
            )
            Text(text = buildAnnotatedString {
                withStyle(SpanStyle().copy(fontSize = 16.sp)) {
                    append("中风险地区：")
                }
                append("\u3000")
                withStyle(SpanStyle().copy(fontSize = 14.sp, color = Color.Yellow)) {
                    append(middleCount)
                    append("个")
                }
            })
        }
    }
}