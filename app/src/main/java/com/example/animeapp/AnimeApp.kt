package com.example.animeapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.animeapp.ui.screens.AnimeSearchScreen
import com.example.animeapp.ui.screens.UserProfileScreen

@Composable
fun AnimeApp(navController: NavHostController = rememberNavController()) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "search") {
        composable("search") { AnimeSearchScreen(navController) }
        composable("profile") { UserProfileScreen(navController) }
    }
}
