package com.example.animeapp.ui.reusableComponents

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.model.Anime
import com.example.animeapp.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AnimeDescriptionDialog(
    animeId: Int,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    var anime by remember { mutableStateOf<Anime?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(animeId) {
        isLoading = true
        error = null
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.apiServiceInstance.searchAnimeById(animeId).execute()
            }
            if (response.isSuccessful) {
                anime = response.body()?.data
            } else {
                error = "Błąd API: ${response.code()}"
            }
        } catch (e: Exception) {
            error = "Błąd: ${e.localizedMessage}"
        }
        isLoading = false
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(anime?.title ?: "Szczegóły anime", style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            if (isLoading) {
                Text("Ładowanie...")
            } else if (error != null) {
                Text(error!!)
            } else if (anime != null) {
                val a = anime!!
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        AsyncImage(
                            model = a.images.jpg.largeImageUrl ?: a.images.jpg.imageUrl ?: a.images.webp.largeImageUrl ?: a.images.webp.imageUrl,
                            contentDescription = "Plakat ${a.title}",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 150.dp, max = 270.dp)
                        )
                    }
                    item { Text("Ocena użytkowników: ${a.score ?: "Brak"}") }
                    item { Text("Status: ${a.status ?: "Brak"}") }
                    item { Text("Odcinki: ${a.episodes ?: "Brak"}") }
                    item { Text("Studio: ${a.studios?.joinToString { it.name } ?: "Brak"}") }
                    item { Text("Gatunki: ${a.genres?.joinToString { it.name } ?: "Brak"}") }
                    a.trailer?.url?.let { trailerUrl ->
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { /* TODO: Otwórz zwiastun */ }) {
                                Text("Zobacz zwiastun")
                            }
                        }
                    }
                    a.synopsis?.let {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(it)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Zamknij")
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}
