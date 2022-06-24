package com.lx.travelprevention.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 加载中
 *
 * @param paddingValues
 */
@Composable
fun LoadingLayout(
    paddingValues: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(30.dp))
    }
}

/**
 * 加载错误
 *
 * @param paddingValues
 * @param throwable 抛出的异常信息
 * @param retry 重试
 * @receiver
 */
@Composable
fun ErrorLayout(
    paddingValues: PaddingValues = PaddingValues(),
    throwable: Throwable = Throwable(),
    retry: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        TextButton(onClick = { retry() }) {
            Text(
                text = throwable.message.orEmpty(), color = MaterialTheme.colors.error
            )
        }
    }
}

/**
 * 加载失败
 *
 * @param paddingValues
 * @param failedMessage 失败信息
 */
@Composable
fun FailedLayout(
    paddingValues: PaddingValues = PaddingValues(),
    failedMessage: String = ""
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Text(text = failedMessage)
    }
}

/**
 * 加载成功
 *
 * @param paddingValues
 * @param content 数据不为空的布局
 * @receiver
 */
@Composable
fun SuccessLayout(
    paddingValues: PaddingValues = PaddingValues(),
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * 空布局
 *
 * @param paddingValues
 * @param content 数据为空的布局
 * @receiver
 */
@Composable
fun EmptyLayout(
    paddingValues: PaddingValues = PaddingValues(),
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = contentAlignment
    ) {
        content()
    }
}