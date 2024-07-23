package com.example.metodohoggsimplificado.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.metodohoggsimplificado.ui.ui.DataInputScreen
import com.example.metodohoggsimplificado.ui.ui.ResultScreen
import com.example.metodohoggsimplificado.viewModel.HoggViewModel

@Composable
fun NavManager(activity: Activity) {
    val navController = rememberNavController()
    val context = activity.applicationContext
    val hoggViewModel:  HoggViewModel = viewModel()

    NavHost(navController = navController, startDestination = "DataInputScreen") {
        composable("DataInputScreen") {
            DataInputScreen(viewModel = hoggViewModel)
        }
        composable( "ResultScreen") {
            ResultScreen(viewModel = hoggViewModel)
        }
    }
}