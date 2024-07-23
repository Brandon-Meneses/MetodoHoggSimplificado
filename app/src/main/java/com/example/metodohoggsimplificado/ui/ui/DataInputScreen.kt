package com.example.metodohoggsimplificado.ui.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.metodohoggsimplificado.viewModel.HoggViewModel
import kotlin.math.pow

@Composable
fun DataInputScreen(viewModel: HoggViewModel) {
    var d0 by remember { mutableStateOf("") }
    var dr by remember { mutableStateOf("") }
    var r by remember { mutableStateOf("") }
    var k by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = d0, onValueChange = { d0 = it }, label = { Text("Deflexión máxima D0 (ej. 45x10-2)") })
        TextField(value = dr, onValueChange = { dr = it }, label = { Text("Deflexión adicional DR (ej. 22x10-2)") })
        TextField(value = r, onValueChange = { r = it }, label = { Text("Distancia radial R (cm)") })
        TextField(value = k, onValueChange = { k = it }, label = { Text("Coeficiente k") })
        Button(onClick = {
            try {
                val d0Value = parseScientificNotation(d0)
                val drValue = parseScientificNotation(dr)
                val rValue = r.toDouble()
                val kValue = k.toDouble()
                viewModel.calculate(d0Value, drValue, rValue, kValue)
            } catch (e: NumberFormatException) {
                // Manejar el error de formato
            }
        }) {
            Text("Calcular")
        }
    }
}
fun parseScientificNotation(input: String): Double {
    return if (input.contains("x10")) {
        val parts = input.split("x10")
        val base = parts[0].toDouble()
        val exponent = parts[1].toInt()
        base * 10.0.pow(exponent)
    } else {
        input.toDouble()
    }
}

