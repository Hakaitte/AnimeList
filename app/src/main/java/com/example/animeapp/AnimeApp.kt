package com.example.animeapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.animeapp.data.UserAnimeStatus
import com.example.animeapp.ui.screens.AnimeSearchScreen
import com.example.animeapp.ui.screens.UserProfileScreen

@Composable
fun AnimeApp(navController: NavHostController = rememberNavController()) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "search") {
        composable("search") { AnimeSearchScreen(navController) }
        composable("profile") { UserProfileScreen(navController) }
        composable("anime_list/{status}") { backStackEntry ->
            val statusArg = backStackEntry.arguments?.getString("status")
            val status = try { UserAnimeStatus.valueOf(statusArg ?: "") } catch (_: Exception) { UserAnimeStatus.WATCHING }
            com.example.animeapp.ui.screens.AnimeListScreen(navController, status)
        }
    }
}
