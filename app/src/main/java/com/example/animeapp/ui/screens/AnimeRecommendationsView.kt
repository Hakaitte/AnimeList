package com.example.animeapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.R
import com.example.animeapp.model.AnimeEntry
import com.example.animeapp.retrofit.RetrofitClient
import com.example.animeapp.ui.reusableComponents.AnimeDescriptionDialog
import kotlinx.coroutines.delay
import retrofit2.awaitResponse
import kotlin.random.Random

@Composable
fun AnimeRecommendationsView() {
    var recommendations by remember { mutableStateOf<List<AnimeEntry>>(emptyList()) }
    var currentIndex by remember { mutableStateOf(0) }
    var visible by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedAnimeId by remember { mutableStateOf<Int?>(null) }

    // Fetch recommendations once
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.apiServiceInstance.getAnimeRecommendations().awaitResponse()
            if (response.isSuccessful) {
                val data = response.body()?.data ?: emptyList()
                // Debug print to logcat
                println("[AnimeRecommendationsView] Received recommendations: ${data.map { it.entry.map { e -> e.title } }}")
                recommendations = data.flatMap { it.entry }.distinctBy { it.malId }
            } else {
                println("[AnimeRecommendationsView] API call failed: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            println("[AnimeRecommendationsView] Exception: ${e.message}")
        }
    }

    LaunchedEffect(recommendations) {
        if (recommendations.isNotEmpty()) {
            currentIndex = Random.nextInt(recommendations.size)
            while (true) {
                visible = true
                delay(4000)
                visible = false
                delay(400)
                currentIndex = (currentIndex + 1) % recommendations.size
            }
        }
    }

    if (recommendations.isNotEmpty()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.recommended_series),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            val anime = recommendations[currentIndex]
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(600)),
                exit = fadeOut(animationSpec = tween(400))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = anime.images.jpg.largeImageUrl ?: anime.images.jpg.imageUrl ?: anime.images.webp.largeImageUrl ?: anime.images.webp.imageUrl,
                        contentDescription = anime.title,
                        modifier = Modifier
                            .size(180.dp, 260.dp)
                            .clickable {
                                selectedAnimeId = anime.malId
                                showDialog = true
                            }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(anime.title, style = MaterialTheme.typography.titleLarge, maxLines = 2)
                }
            }
        }
    } else {
        Text(stringResource(id = R.string.loading_recommendations))
    }

    if (showDialog && selectedAnimeId != null) {
        AnimeDescriptionDialog(animeId = selectedAnimeId!!, onDismissRequest = { showDialog = false })
    }
}
