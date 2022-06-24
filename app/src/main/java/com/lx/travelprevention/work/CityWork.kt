package com.lx.travelprevention.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.lx.travelprevention.common.DataResult.Companion.isSuccess
import com.lx.travelprevention.dao.CityDao
import com.lx.travelprevention.network.ApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CityWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val apiService: ApiService,
    private val dao: CityDao
) : CoroutineWorker(appContext, params) {

    companion object {
        private const val TAG = "CityWork"
    }

    override suspend fun doWork(): Result {
        return try {
            val result = apiService.cities()
            Log.w(TAG, "doWork: city size is ${result.data?.size}")
            if (result.isSuccess) {
                val cities = result.data?.flatMap { parent ->
                    parent.citys.map { it.copy(parent = parent.province) }
                }.orEmpty()
                dao.saveCity(cities).apply {
                    Log.w(TAG, "doWork: save city state is $this")
                }
                Result.success(workDataOf("state" to true))
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}