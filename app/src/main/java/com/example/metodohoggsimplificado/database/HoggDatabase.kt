package com.example.metodohoggsimplificado.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.metodohoggsimplificado.entidades.Coefficient
import com.example.metodohoggsimplificado.entidades.E0Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

@Database(entities = [Coefficient::class, E0Value::class], version = 1)
abstract class HoggDatabase : RoomDatabase() {
    abstract fun hoggDao(): HoggDao

    companion object {
        @Volatile
        private var INSTANCE: HoggDatabase? = null

        fun getDatabase(context: Context): HoggDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HoggDatabase::class.java,
                    "hogg-database"
                )
                    .createFromAsset("hogg-database.db")
                    .fallbackToDestructiveMigration()
                    .build()



                INSTANCE = instance
                instance
            }
        }
    }
}


fun populateDatabase(context: Context, db: SupportSQLiteDatabase) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            context.assets.open("data.sql").use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        if (line!!.trim().isNotEmpty()) {
                            try {
                                db.execSQL(line!!)
                                Log.d("Database", "Executed SQL: $line")
                            } catch (e: Exception) {
                                Log.e("Database", "Error executing SQL: $line", e)
                            }
                        }
                    }
                }
            }
            Log.d("Database", "Database populated successfully from data.sql")
        } catch (e: Exception) {
            Log.e("Database", "Error populating database from data.sql", e)
        }
    }
}

