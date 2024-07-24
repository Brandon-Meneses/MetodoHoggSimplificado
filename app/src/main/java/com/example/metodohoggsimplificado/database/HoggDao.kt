package com.example.metodohoggsimplificado.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.metodohoggsimplificado.entidades.Coefficient
import com.example.metodohoggsimplificado.entidades.E0Value
import com.example.metodohoggsimplificado.viewModel.HoggViewModel

@Dao
interface HoggDao {
    @Query("SELECT * FROM coefficients")
    suspend fun getAllCoefficients(): List<Coefficient>

    @Query("SELECT * FROM e0_values")
    suspend fun getAllE0Values(): List<E0Value>
    @Query("SELECT * FROM coefficients WHERE d0dr = :d0dr")
    fun getCoefficient(d0dr: Double): Coefficient?

    @Query("SELECT e0 FROM e0_values WHERE d0r50 <= :d0r50 ORDER BY d0r50 DESC LIMIT 1")
    suspend fun getClosestE0(d0r50: Double): Int?

    @Insert
    suspend fun insertCoefficients(coefficients: List<Coefficient>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertE0Values(e0Values: List<E0Value>)
}



