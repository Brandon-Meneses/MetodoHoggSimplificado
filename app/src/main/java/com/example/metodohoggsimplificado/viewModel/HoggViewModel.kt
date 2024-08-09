package com.example.metodohoggsimplificado.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.metodohoggsimplificado.database.HoggDao
import com.example.metodohoggsimplificado.database.HoggDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HoggViewModel(application: Application) : AndroidViewModel(application) {
    private val db: HoggDatabase = HoggDatabase.getDatabase(application)
    private val hoggDao: HoggDao = db.hoggDao()

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    init {
        viewModelScope.launch {
            printDatabaseValues()
        }
    }

    private suspend fun printDatabaseValues() {
        withContext(Dispatchers.IO) {
            val coefficients = hoggDao.getAllCoefficients()
            if (coefficients.isEmpty()) {
                Log.e("HoggViewModel", "No coefficients found in the database.")
            } else {
                for (coefficient in coefficients) {
                    Log.d("HoggDatabase", "Coefficient: id=${coefficient.id}, d0dr=${coefficient.d0dr}, r20=${coefficient.r20}, r30=${coefficient.r30}, r40=${coefficient.r40}, r50=${coefficient.r50}, r60=${coefficient.r60}, r70=${coefficient.r70}, r80=${coefficient.r80}, r90=${coefficient.r90}, r100=${coefficient.r100}")
                }
            }

            val e0Values = hoggDao.getAllE0Values()
            if (e0Values.isEmpty()) {
                Log.e("HoggViewModel", "No E0 values found in the database.")
            } else {
                for (e0Value in e0Values) {
                    Log.d("HoggDatabase", "E0Value: d0r50=${e0Value.d0r50}, e0=${e0Value.e0}")
                }
            }
        }
    }

    fun calculate(d0: Double, dr: Double, r: Double, k: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val adjustedD0 = adjustValue(d0)
            val adjustedDr = adjustValue(dr)

            val d0dr = String.format("%.2f", adjustedD0 / adjustedDr).toDouble()
            Log.d("HoggViewModel", "d0dr: $d0dr")
            val coefficient = hoggDao.getCoefficient(d0dr)
            if (coefficient == null) {
                Log.e("HoggViewModel", "Coefficient not found for d0dr: $d0dr")
                _result.postValue(Result(error = "Valor D0/DR fuera del rango de la tabla"))
                return@launch
            }
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
                else -> {
                    Log.e("HoggViewModel", "Invalid R value: $r")
                    _result.postValue(Result(error = "R debe ser uno de los valores: 20, 30, 40, 50, 60, 70, 80, 90, 100"))
                    return@launch
                }
            }
            Log.d("HoggViewModel", "r50: $r50")

            val d0WithoutScale = adjustedD0 * 100  // Multiplica por 100 para eliminar "x10-2"
            val d0r50 = String.format("%.1f", d0WithoutScale * r50).toDouble()
            Log.d("HoggViewModel", "d0r50: $d0r50")

            val e0 = calculateE0(d0r50)
            Log.d("HoggViewModel", "e0: $e0")

            val cbr = e0 / k
            Log.d("HoggViewModel", "cbr: $cbr")

            val cbrPercentage = roundToNearestInteger(cbr)
            Log.d("HoggViewModel", "cbrPercentage: $cbrPercentage%")
            _result.postValue(Result(e0 = e0, cbr = cbrPercentage))
        }
    }

    private fun adjustValue(value: Double): Double {
        // Si el valor es mayor que 1, asumimos que está en la escala correcta
        return if (value > 1) value / 100 else value
    }

    private suspend fun calculateE0(d0r50: Double): Int {
        val closestE0Value = hoggDao.getClosestE0(d0r50)
        Log.d("HoggViewModel", "Closest E0Value: $closestE0Value for d0r50: $d0r50")
        return closestE0Value ?: throw IllegalStateException("Valor D0*R50 fuera del rango de la tabla")
    }

    private fun roundToNearestInteger(value: Double): Int {
        val roundedValue = String.format("%.3f", value).toDouble() // Redondear al tercer decimal
        return Math.round(roundedValue).toInt() // Convertir a entero redondeado
    }

    data class Result(val e0: Int? = null, val cbr: Int? = null, val error: String? = null)
}

