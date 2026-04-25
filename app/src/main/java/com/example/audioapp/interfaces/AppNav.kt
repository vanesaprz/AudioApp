package com.example.audioapp.interfaces

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

object Routes {
    const val HOME = "home"
    const val AUDIO = "audio"
}

@Composable
fun AppNav(navController: NavHostController) {
    //definimos el panel de navegación de la aplicación
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.AUDIO) { AudioScreen(navController) }
    }
}


