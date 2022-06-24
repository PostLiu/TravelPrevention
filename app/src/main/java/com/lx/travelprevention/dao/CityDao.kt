package com.lx.travelprevention.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lx.travelprevention.model.entity.City
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCity(list: List<City>)

    @Query("SELECT * FROM CITY")
    fun allCities(): Flow<List<City>>
}