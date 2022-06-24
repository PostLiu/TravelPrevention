package com.lx.travelprevention.di

import android.content.Context
import com.lx.travelprevention.dao.CityDao
import com.lx.travelprevention.dao.TravelPreventionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TravelPreventionDatabaseModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): TravelPreventionDatabase {
        return TravelPreventionDatabase.create(context)
    }

    @Singleton
    @Provides
    fun provideCityDao(database: TravelPreventionDatabase): CityDao {
        return database.cityDao()
    }
}