package com.example.metodohoggsimplificado.ui.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.metodohoggsimplificado.viewModel.HoggViewModel

@Composable
fun ResultScreen(viewModel: HoggViewModel,navController: NavController) {
    val result by viewModel.result.observeAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        result?.let {
            Text("Módulo Resiliente (E0): ${it.e0} kg/cm2")
            Text("Valor de Soporte (CBR): ${it.cbr} %")
        } ?: run {
            Text("Ingrese los datos para ver los resultados")
        }
    }
}
