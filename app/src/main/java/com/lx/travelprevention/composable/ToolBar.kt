package com.lx.travelprevention.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun ToolBar(
    modifier: Modifier = Modifier,
    title: String,
    showBackIcon: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    onBack: (() -> Unit)? = null
) {

    val back = @Composable {
        IconButton(onClick = {
            onBack?.invoke()
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
    }
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = if (showBackIcon) back else null,
        actions = { actions.invoke(this) },
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    )
}