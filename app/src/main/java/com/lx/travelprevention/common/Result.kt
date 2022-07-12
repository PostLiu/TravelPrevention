@file:Suppress("unused")

package com.lx.travelprevention.common

import com.google.gson.annotations.SerializedName

data class DataResult<out T>(
    @SerializedName("error_code") val code: Int,
    @SerializedName("reason") val message: String,
    @SerializedName("result") val `data`: T?
) {
    companion object {
        inline val <T> DataResult<T>.isSuccess get() = code == 0
    }
}

sealed class StateResult<out T : Any> {
    object Loading : StateResult<Nothing>()
    data class Failed(val message: String) : StateResult<Nothing>()
    data class Success<T : Any>(val data: T?) : StateResult<T>()
    data class Error(val throwable: Throwable) : StateResult<Nothing>()

    companion object {
        inline val StateResult<Nothing>.isLoading get() = this is Loading
        inline val <T : Any> StateResult<T>.result get() = if (this is Success) data else null
        inline val StateResult<Nothing>.message get() = if (this is Failed) message else null
        inline val StateResult<Nothing>.error get() = if (this is Error) throwable else null
    }
}