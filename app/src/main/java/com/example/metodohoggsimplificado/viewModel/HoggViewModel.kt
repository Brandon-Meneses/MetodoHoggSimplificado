package com.example.metodohoggsimplificado.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.metodohoggsimplificado.database.HoggDatabase
import com.example.metodohoggsimplificado.entidades.Coefficient
import com.example.metodohoggsimplificado.entidades.E0Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HoggViewModel(application: Application) : AndroidViewModel(application) {
    private val db: HoggDatabase = HoggDatabase.getDatabase(application)

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result



    fun calculate(d0: Double, dr: Double, r: Double, k: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val d0dr = String.format("%.2f", d0 / dr).toDouble()
            Log.d("HoggViewModel", "d0dr: $d0dr")
            val coefficient = db.hoggDao().getCoefficient(d0dr) ?: throw IllegalStateException("Valor D0/DR fuera del rango de la tabla")
            Log.d("HoggViewModel", "Coefficient: $coefficient")

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
            Log.d("HoggViewModel", "r50: $r50")

            val d0WithoutScale = d0 * 100  // Multiplica por 100 para eliminar "x10-2"
            val d0r50 = String.format("%.1f", d0WithoutScale * r50).toDouble()
            Log.d("HoggViewModel", "d0r50: $d0r50")

            val e0 = calculateE0(d0r50)
            Log.d("HoggViewModel", "e0: $e0")

            val cbr = e0 / k
            Log.d("HoggViewModel", "cbr: $cbr")

            val cbrPercentage = roundToNearestInteger(cbr)
            Log.d("HoggViewModel", "cbrPercentage: $cbrPercentage%")
            _result.postValue(Result(e0, cbrPercentage))
        }
    }

    private suspend fun calculateE0(d0r50: Double): Int {
        val closestE0Value = db.hoggDao().getClosestE0(d0r50)
        Log.d("HoggViewModel", "Closest E0Value: $closestE0Value for d0r50: $d0r50")
        return closestE0Value ?: throw IllegalStateException("Valor D0*R50 fuera del rango de la tabla")
    }

    private fun roundToNearestInteger(value: Double): Int {
        val roundedValue = String.format("%.3f", value).toDouble() // Redondear al tercer decimal
        return Math.round(roundedValue).toInt() // Convertir a entero redondeado
    }

    data class Result(val e0: Int, val cbr: Int)
}


