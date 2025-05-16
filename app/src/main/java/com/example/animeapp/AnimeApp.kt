package com.example.animeapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.animeapp.ui.screens.anime.AnimeSearchScreen

@Composable
fun AnimeApp(navController: NavHostController = rememberNavController()) {
    AnimeSearchScreen()
}