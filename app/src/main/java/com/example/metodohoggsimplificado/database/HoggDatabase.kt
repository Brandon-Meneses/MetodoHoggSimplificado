package com.example.metodohoggsimplificado.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.metodohoggsimplificado.entidades.Coefficient
import com.example.metodohoggsimplificado.entidades.E0Value

@Database(entities = [Coefficient::class, E0Value::class], version = 1)
abstract class HoggDatabase : RoomDatabase() {
    abstract fun hoggDao(): HoggDao
}
