package com.example.metodohoggsimplificado.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.metodohoggsimplificado.entidades.Coefficient
import com.example.metodohoggsimplificado.entidades.E0Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//fun prepopulateDatabase(context: Context) {
//    val db = HoggDatabase.getDatabase(context)
//    CoroutineScope(Dispatchers.IO).launch {
//        val hoggDao = db.hoggDao()
//        val coefficients = listOf(
//            Coefficient(d0dr = 3.98, r20 = 11.9, r30 = 17.8, r40 = 23.5, r50 = 29.3, r60 = 35.2, r70 = 40.4, r80 = 46.6, r90 = 53.1, r100 = 59.5),
//            // Completar con todos los datos necesarios
//        )
//        hoggDao.insertCoefficients(coefficients)
//        Log.d("PrepopulateDatabase", "Coefficients inserted: $coefficients")
//
//        val e0Values = listOf(
//            E0Value(d0r50 = 300.0, e0 = 5150),
//            E0Value(d0r50 = 305.0, e0 = 5070),
//            E0Value(d0r50 = 310.0, e0 = 4994),
//            E0Value(d0r50 = 315.0, e0 = 4919),
//            E0Value(d0r50 = 320.0, e0 = 4847),
//            E0Value(d0r50 = 325.0, e0 = 4777),
//            E0Value(d0r50 = 330.0, e0 = 4709),
//            // Completar con todos los datos necesarios
//        )
//        hoggDao.insertE0Values(e0Values)
//        Log.d("PrepopulateDatabase", "E0Values inserted: $e0Values")
//    }
//}