package com.lx.travelprevention.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lx.travelprevention.model.entity.City
import com.lx.travelprevention.work.CityWork

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class TravelPreventionDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {
        private var INSTANCE: TravelPreventionDatabase? = null

        fun create(context: Context): TravelPreventionDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): TravelPreventionDatabase {
            return Room.databaseBuilder(
                context, TravelPreventionDatabase::class.java, "travel_prevention.db"
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val request = OneTimeWorkRequestBuilder<CityWork>().addTag("city").build()
                    WorkManager.getInstance(context).enqueue(request)
                }
            }).build()
        }
    }
}