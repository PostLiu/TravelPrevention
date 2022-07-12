package com.lx.travelprevention.ui.set.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lx.travelprevention.model.entity.ThemeType

@Composable
fun MultipleThemesPage(
    navController: NavController = rememberNavController(),
    themeType: ThemeType = ThemeType.PURPLE,
    onSelected: ((ThemeType) -> Unit)? = null
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "更换主题")
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(4), modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(ThemeType.defaultThemes) {
                Column(
                    Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(Color(it.color))
                            .clickable {
                                onSelected?.invoke(it)
                            }, contentAlignment = Alignment.Center

                    ) {
                        if (themeType == it) {
                            Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                        }
                    }
                    Text(text = it.labelName)
                }
            }
        }
    }
}