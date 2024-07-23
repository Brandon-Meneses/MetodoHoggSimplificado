package com.example.metodohoggsimplificado.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.metodohoggsimplificado.database.HoggDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode

class HoggViewModel(application: Application) : AndroidViewModel(application) {
    private val db: HoggDatabase = Room.databaseBuilder(
        application,
        HoggDatabase::class.java, "hogg-database"
    ).build()

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    fun calculate(d0: Double, dr: Double, r: Double, k: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val d0dr = (d0 / dr).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
            val coefficient = db.hoggDao().getCoefficient(d0dr) ?: throw IllegalStateException("Valor D0/DR fuera del rango de la tabla")

            val r50 = when (r.toInt()) {
                20 -> coefficient.r20
                30 -> coefficient.r30
                40 -> coefficient.r40
                50 -> coefficient.r50
                60 -> coefficient.r60
                70 -> coefficient.r70
                80 -> coefficient.r80
                90 -> coefficient.r90
                100 -> coefficient.r100
                else -> throw IllegalArgumentException("R debe ser uno de los valores: 20, 30, 40, 50, 60, 70, 80, 90, 100")
            }

            val d0r50 = (d0 * r50).toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()
            val e0 = calculateE0(d0r50)
            val cbr = e0 / k
            _result.postValue(Result(e0, cbr))
        }
    }

    private suspend fun calculateE0(d0r50: Double): Double {
        return db.hoggDao().getE0(d0r50)?.toDouble() ?: throw IllegalStateException("Valor D0*R50 fuera del rango de la tabla")
    }

    data class Result(val e0: Double, val cbr: Double)
}


