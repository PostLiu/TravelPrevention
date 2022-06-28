package com.lx.travelprevention.ui.policy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lx.travelprevention.common.StateResult
import com.lx.travelprevention.composable.EmptyLayout
import com.lx.travelprevention.composable.ErrorLayout
import com.lx.travelprevention.composable.FailedLayout
import com.lx.travelprevention.composable.LoadingLayout

@Composable
fun HealthyTravelPolicyPage(
    navController: NavController = rememberNavController(),
    healthy: StateResult<Any> = StateResult.Loading
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
                ErrorLayout(paddingValues = paddingValues)
            }
            is StateResult.Failed -> {
                FailedLayout(paddingValues = paddingValues)
            }
            is StateResult.Success -> {
                if (healthy.data == null) {
                    EmptyLayout(paddingValues = paddingValues) {

                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = paddingValues)
                    ) {

                    }
                }
            }
        }
    }
}