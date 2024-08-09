package com.example.metodohoggsimplificado.ui.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.metodohoggsimplificado.R
import com.example.metodohoggsimplificado.database.HoggDatabase
import com.example.metodohoggsimplificado.viewModel.HoggViewModel
import kotlin.math.pow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataInputScreen(viewModel: HoggViewModel, navController: NavController) {
    var d0 by remember { mutableStateOf("") }
    var dr by remember { mutableStateOf("") }
    var r by remember { mutableStateOf("") }
    var k by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Método de Hogg Simplificado") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFFD700) // Amarillo
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()) // Considerar el padding del Scaffold
        ) {
            Divider(color = Color.Black, thickness = 2.dp) // Línea separadora

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .imePadding(), // Asegura que el contenido se ajusta cuando el teclado está visible
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .border(
                                2.dp,
                                Color.Black,
                                shape = RoundedCornerShape(8.dp)
                            ) // Aquí defines el marco
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.fondo),
                            contentDescription = null,
                            modifier = Modifier.size(200.dp),
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(50.dp)) }
                item {
                    OutlinedTextField(
                        value = d0,
                        onValueChange = {
                            d0 = it
                            showError = false
                        },
                        label = { Text("Deflexión máxima D0 en mm (ej. 45x10-2 )") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF8B4513), // Marrón
                            unfocusedBorderColor = Color(0xFF8B4513) // Marrón
                        )
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    OutlinedTextField(
                        value = dr,
                        onValueChange = {
                            dr = it
                            showError = false
                        },
                        label = { Text("Deflexión adicional DR en mm (ej. 22x10-2)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF8B4513), // Marrón
                            unfocusedBorderColor = Color(0xFF8B4513) // Marrón
                        )
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    OutlinedTextField(
                        value = r,
                        onValueChange = {
                            r = it
                            showError = false
                        },
                        label = { Text("Distancia radial R en cm") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF8B4513), // Marrón
                            unfocusedBorderColor = Color(0xFF8B4513) // Marrón
                        )
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    OutlinedTextField(
                        value = k,
                        onValueChange = {
                            k = it
                            showError = false
                        },
                        label = { Text("Coeficiente k") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF8B4513), // Marrón
                            unfocusedBorderColor = Color(0xFF8B4513) // Marrón
                        )
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    if (showError) {
                        Text(
                            text = "Por favor, ingrese valores válidos.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Button(
                        onClick = {
                            try {
                                val d0Value = parseScientificNotation(d0)
                                val drValue = parseScientificNotation(dr)
                                val rValue = r.toDouble()
                                val kValue = k.toDouble()
                                viewModel.calculate(d0Value, drValue, rValue, kValue)
                                navController.navigate("ResultScreen")
                            } catch (e: NumberFormatException) {
                                showError = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8B4513) // Marrón
                        )
                    ) {
                        Text("Calcular", color = Color.White)
                    }
                }
            }
        }
    }
}




@Composable
fun titleBar(name: String) {
    Text(
        text = name,
        fontSize = 30.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
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

