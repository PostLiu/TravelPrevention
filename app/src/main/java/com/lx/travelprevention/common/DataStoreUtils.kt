@file:Suppress("unused")

package com.lx.travelprevention.common

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "setting")

class DataStoreUtils(private val context: Context) {

    companion object {

        fun instance(context: Context) = DataStoreUtils(context)

    }

    suspend fun writeInt(key: String, value: Int) = context.dataStore.edit {
        it[intPreferencesKey(key)] = value
    }

    suspend fun writeDouble(key: String, value: Double) = context.dataStore.edit {
        it[doublePreferencesKey(key)] = value
    }

    suspend fun writeString(key: String, value: String) = context.dataStore.edit {
        it[stringPreferencesKey(key)] = value
    }

    suspend fun writeBoolean(key: String, value: Boolean) = context.dataStore.edit {
        it[booleanPreferencesKey(key)] = value
    }

    suspend fun writeFloat(key: String, value: Float) = context.dataStore.edit {
        it[floatPreferencesKey(key)] = value
    }

    suspend fun writeLong(key: String, value: Long) = context.dataStore.edit {
        it[longPreferencesKey(key)] = value
    }

    suspend fun writeStringSet(key: String, value: Set<String>) = context.dataStore.edit {
        it[stringSetPreferencesKey(key)] = value
    }

    suspend fun readInt(key: String) =
        context.dataStore.data.map { it[intPreferencesKey(key)] }.firstOrNull()

    suspend fun readDouble(key: String) =
        context.dataStore.data.map { it[doublePreferencesKey(key)] }.firstOrNull()

    suspend fun readString(key: String) =
        context.dataStore.data.map { it[stringSetPreferencesKey(key)] }.firstOrNull()

    suspend fun readBoolean(key: String) =
        context.dataStore.data.map { it[booleanPreferencesKey(key)] }.firstOrNull()

    suspend fun readFloat(key: String) =
        context.dataStore.data.map { it[floatPreferencesKey(key)] }.firstOrNull()

    suspend fun readLong(key: String) =
        context.dataStore.data.map { it[longPreferencesKey(key)] }.firstOrNull()

    suspend fun readStringSet(key: String) =
        context.dataStore.data.map { it[stringSetPreferencesKey(key)] }.firstOrNull()


}