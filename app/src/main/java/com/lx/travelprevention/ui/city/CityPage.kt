@file:OptIn(ExperimentalFoundationApi::class)

package com.lx.travelprevention.ui.city

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.model.entity.City
import com.lx.travelprevention.model.entity.CityEntity
import com.lx.travelprevention.ui.theme.TravelPreventionTheme
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun CityPage(
    navController: NavController = rememberNavController(),
    type: String = "",
    cities: StateResult<List<City>> = StateResult.Loading,
    retry: () -> Unit = {},
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "查询城市数据") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->
        when (cities) {
            is StateResult.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = { retry() }) {
                        Text(
                            text = cities.throwable.message.orEmpty(),
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
            is StateResult.Failed -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),

                    contentAlignment = Alignment.Center
                ) {
                    Text(text = cities.message)
                }
            }
            is StateResult.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(30.dp))
                }
            }
            is StateResult.Success -> {
                LoadingSuccess(paddingValues = paddingValues,
                    header = cities.data?.map { it.parent }.orEmpty(),
                    cities = cities.data.orEmpty(),
                    itemClick = {
                        val key = when (type) {
                            "from" -> "from"
                            "to" -> "to"
                            else -> "city_id"
                        }
                        val value = when (type) {
                            "from" -> listOf(it.cityId, it.city).joinToString("|")
                            "to" -> listOf(it.cityId, it.city).joinToString("|")
                            else -> it.cityId
                        }
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            key = key,
                            value = value
                        )
                        navController.popBackStack()
                    })
            }
        }
    }
}

@Composable
fun LoadingSuccess(
    paddingValues: PaddingValues,
    header: List<String>,
    cities: List<City> = emptyList(),
    itemClick: (City) -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    val state = rememberLazyListState()
    if (header.isNotEmpty()) {
        LaunchedEffect(key1 = state, block = {
            snapshotFlow { state.firstVisibleItemIndex }.collect {
                println("FirstIndex is $it")
                name = header[it]
            }
        })
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues), state = state
    ) {
        if (name.isNotEmpty()) {
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp)
                        .background(MaterialTheme.colors.background)
                        .padding(12.dp)
                ) {
                    Text(text = name)
                }
            }
        }
        items(cities) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable { itemClick(it) }) {
                Text(text = it.city)
            }
            Divider()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CityPreview() {
    TravelPreventionTheme {
        val json = LocalContext.current.assets.open("city.json").use { inputStream ->
            val json = StringBuilder()
            var line: String? = null
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            while (bufferedReader.readLine()?.also { line = it } != null) {
                json.append(line)
            }
            json.toString()
        }
        val dataList =
            Gson().fromJson<List<CityEntity>>(json, object : TypeToken<List<CityEntity>>() {}.type)
        println("城市数据：$dataList")
        val cities = dataList.flatMap { parent ->
            parent.citys.map { it.copy(parent = parent.province) }
        }
        val header = cities.map { it.parent }
        LoadingSuccess(paddingValues = PaddingValues(), header = header, cities = cities)
    }
}