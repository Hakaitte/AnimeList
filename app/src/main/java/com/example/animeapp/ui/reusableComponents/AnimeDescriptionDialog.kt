package com.example.animeapp.ui.reusableComponents

import android.widget.Toast
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.animeapp.AnimeListViewModel
import com.example.animeapp.R
import com.example.animeapp.model.Anime
import com.example.animeapp.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AnimeDescriptionDialog(
    animeId: Int,
    onDismissRequest: () -> Unit,
    animeListViewModel: AnimeListViewModel = viewModel(),
    showAddToListButton: Boolean = true
) {
    val context = LocalContext.current
    var anime by remember { mutableStateOf<Anime?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var showDetailDialog by remember { mutableStateOf(false) }

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
                error = context.getString(R.string.api_error)
            }
        } catch (e: Exception) {
            error = context.getString(R.string.error, e.localizedMessage ?: "")
        }
        isLoading = false
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(anime?.title ?: stringResource(id = R.string.anime_details), style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            if (isLoading) {
                Text(stringResource(id = R.string.loading))
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
                            contentDescription = stringResource(id = R.string.poster_content_description, a.title),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 150.dp, max = 270.dp)
                        )
                    }
                    item { Text(stringResource(id = R.string.user_rating, a.score?.toString() ?: context.getString(R.string.none))) }
                    item { Text(stringResource(id = R.string.status, a.status ?: context.getString(R.string.none))) }
                    item { Text(stringResource(id = R.string.episodes, a.episodes?.toString() ?: context.getString(R.string.none))) }
                    item { Text(stringResource(id = R.string.studio, a.studios?.joinToString { it.name } ?: context.getString(R.string.none))) }
                    item { Text(stringResource(id = R.string.genres, a.genres?.joinToString { it.name } ?: context.getString(R.string.none))) }
                    a.trailer?.url?.let { trailerUrl ->
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            val context = LocalContext.current
                            Button(onClick = {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(trailerUrl))
                                context.startActivity(intent)
                            }) {
                                Text(stringResource(id = R.string.watch_trailer))
                            }
                        }
                    }
                    if (showAddToListButton) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { showDetailDialog = true }) {
                                Text(stringResource(id = R.string.add_to_list))
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
                Text(stringResource(id = R.string.close))
            }
        },
        modifier = Modifier.padding(16.dp)
    )
    if (showDetailDialog && anime != null) {
        AnimeDetailDialog(
            anime = anime!!,
            initialUserScore = null,
            initialStatus = com.example.animeapp.data.UserAnimeStatus.NONE,
            onDismissRequest = { showDetailDialog = false },
            onStatusChange = { newStatus, newScore ->
                animeListViewModel.addOrUpdateUserAnime(
                    animeFromApi = anime!!,
                    status = newStatus,
                    userScore = newScore
                )
                showDetailDialog = false
                Toast.makeText(
                    context,
                    context.getString(R.string.saved_as, anime!!.title, newStatus.name.lowercase().replaceFirstChar { it.titlecase() }, newScore ?: context.getString(R.string.none)),
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}