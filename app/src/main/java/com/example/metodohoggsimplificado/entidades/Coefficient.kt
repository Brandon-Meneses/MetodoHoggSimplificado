package com.example.metodohoggsimplificado.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coefficients")
data class Coefficient(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val d0dr: Double,
    val r20: Double,
    val r30: Double,
    val r40: Double,
    val r50: Double,
    val r60: Double,
    val r70: Double,
    val r80: Double,
    val r90: Double,
    val r100: Double
)