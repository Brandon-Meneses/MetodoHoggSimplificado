package com.example.metodohoggsimplificado.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.metodohoggsimplificado.entidades.Coefficient
import com.example.metodohoggsimplificado.entidades.E0Value

@Database(entities = [Coefficient::class, E0Value::class], version = 2)
abstract class HoggDatabase : RoomDatabase() {
    abstract fun hoggDao(): HoggDao

    companion object {
        @Volatile
        private var INSTANCE: HoggDatabase? = null

        // Definición de la migración
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Eliminar tablas antiguas (si es necesario)
                database.execSQL("DROP TABLE IF EXISTS coefficients")
                database.execSQL("DROP TABLE IF EXISTS e0_values")

                // Crear nuevas tablas
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `coefficients` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `d0dr` REAL NOT NULL, 
                        `r20` REAL NOT NULL, 
                        `r30` REAL NOT NULL, 
                        `r40` REAL NOT NULL, 
                        `r50` REAL NOT NULL, 
                        `r60` REAL NOT NULL, 
                        `r70` REAL NOT NULL, 
                        `r80` REAL NOT NULL, 
                        `r90` REAL NOT NULL, 
                        `r100` REAL NOT NULL
                    )
                """.trimIndent())

                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `e0_values` (
                        `d0r50` REAL NOT NULL, 
                        `e0` INTEGER NOT NULL, 
                        PRIMARY KEY(`d0r50`)
                    )
                """.trimIndent())
            }
        }

        fun getDatabase(context: Context): HoggDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HoggDatabase::class.java,
                    "hogg-database"
                )
                    .addMigrations(MIGRATION_1_2) // Agregar la migración aquí
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
