package com.lx.travelprevention

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lx.travelprevention.common.Constant
import com.lx.travelprevention.common.DataStoreUtils
import com.lx.travelprevention.common.Routes
import com.lx.travelprevention.composable.*
import com.lx.travelprevention.model.entity.ThemeType
import com.lx.travelprevention.ui.theme.TravelPreventionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private val dataStoreUtils by lazy { DataStoreUtils.instance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selectedTheme by remember { mutableStateOf(ThemeType.PURPLE) }
            var saveColor by remember { mutableStateOf(Color(ThemeType.PURPLE.color)) }
            LaunchedEffect(key1 = Unit, block = {
                Log.e("onCreate", "onCreate: get color from preference")
                saveColor = Color(dataStoreUtils.readLong(Constant.THEME) ?: ThemeType.PURPLE.color)
                selectedTheme = ThemeType.defaultThemes.findLast { Color(it.color) == saveColor }
                    ?: ThemeType.PURPLE
            })
            // 读取缓存的颜色
            LaunchedEffect(key1 = selectedTheme, block = {
                Log.e(TAG, "onCreate: save color to preference")
                dataStoreUtils.writeLong(Constant.THEME, selectedTheme.color)
            })
            val primaryColor = Color(selectedTheme.color)
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(primaryColor)
            }
            TravelPreventionTheme(themeType = selectedTheme) {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = Routes.Home) {
                        // 主页
                        main(navController = navController, dataStoreUtils = dataStoreUtils)
                        // 城市
                        city(navController)
                        // 核酸机构
                        agency(navController)
                        // 风险地区
                        area(navController)
                        // 健康出行政策
                        policy(navController)
                        // 主题色
                        theme(navController, selectedTheme) {
                            selectedTheme = it
                        }
                    }
                }
            }
        }
    }
}