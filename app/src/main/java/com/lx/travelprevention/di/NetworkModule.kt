package com.lx.travelprevention.di

import com.google.gson.Gson
import com.google.gson.ToNumberPolicy
import com.lx.travelprevention.BuildConfig
import com.lx.travelprevention.common.Constant
import com.lx.travelprevention.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder().baseUrl(Constant.IP).client(
            OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
                ).build()
        ).addConverterFactory(
            GsonConverterFactory.create(
                Gson().newBuilder().setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
                    .create()
            )
        ).build().create(ApiService::class.java)
    }
}