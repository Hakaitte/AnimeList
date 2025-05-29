package com.example.animeapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.animeapp.AnimeListViewModel
import com.example.animeapp.R
import com.example.animeapp.data.UserAnimeStatus
import com.example.animeapp.model.Anime
import com.example.animeapp.model.AnimeResponse
import com.example.animeapp.retrofit.RetrofitClient
import com.example.animeapp.ui.reusableComponents.AnimeDetailDialog
import com.example.animeapp.ui.reusableComponents.BottomNavigationBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeSearchScreen(
    navController: NavController = rememberNavController(),
    animeListViewModel: AnimeListViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var animeList by remember { mutableStateOf<List<Anime>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var searchPerformed by remember { mutableStateOf(false) }
    var isSfwChecked by remember { mutableStateOf(true) }
    var showAnimeDetailDialog by remember { mutableStateOf(false) }
    var selectedAnimeForDialog by remember { mutableStateOf<Anime?>(null) }
    val context = LocalContext.current

    val handleAnimeClick = { animeFromApi: Anime ->
        selectedAnimeForDialog = animeFromApi
        showAnimeDetailDialog = true
    }

    fun performSearch(query: String, sfw: Boolean) {
        if (query.isBlank()) {
            animeList = emptyList()
            errorMessage = context.getString(R.string.enter_title_to_search)
            searchPerformed = false
            return
        }

        isLoading = true
        errorMessage = null
        searchPerformed = true

        RetrofitClient.apiServiceInstance.searchAnime(query = query, safeForWork = sfw)
            .enqueue(object : Callback<AnimeResponse> {
                override fun onResponse(
                    call: Call<AnimeResponse>,
                    response: Response<AnimeResponse>
                ) {
                    isLoading = false
                    if (response.isSuccessful) {
                        val animeData = response.body()?.data
                        if (animeData != null) {
                            val uniqueAnimeData = animeData.distinctBy { it.malId }
                            if (uniqueAnimeData.isEmpty() && animeData.isNotEmpty()){
                                errorMessage = context.getString(R.string.duplicates_found)
                            } else {
                                errorMessage = null
                            }

                            animeList = uniqueAnimeData
                            if (animeData.isEmpty()) {
                                errorMessage = context.getString(R.string.no_results_for, query)
                            }
                        } else {
                            animeList = emptyList()
                            errorMessage = context.getString(R.string.empty_response)
                        }
                    } else {
                        animeList = emptyList()
                        errorMessage = context.getString(R.string.error_code_message, response.code(), response.message())
                        try {
                            val errorBody = response.errorBody()?.string()
                            Log.e("AnimeSearchScreen", "Error Body: $errorBody")
                            if (!errorBody.isNullOrBlank()) {
                                errorMessage += "\n" + context.getString(R.string.details, errorBody)
                            }
                        } catch (e: Exception) {
                            Log.e("AnimeSearchScreen", "Error parsing error body", e)
                        }
                    }
                }

                override fun onFailure(call: Call<AnimeResponse>, t: Throwable) {
                    isLoading = false
                    animeList = emptyList()
                    errorMessage = context.getString(R.string.connection_error, t.message ?: "")
                    Log.e("AnimeSearchScreen", "API Call Failed", t)
                }
            })
    }

    val currentRoute = navController.currentDestination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.anime_search_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = currentRoute)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(stringResource(id = R.string.search_anime_label)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { isSfwChecked = !isSfwChecked }
                        .padding(horizontal = 4.dp)
                ) {
                    Checkbox(
                        checked = isSfwChecked,
                        onCheckedChange = { isSfwChecked = it }
                    )
                    Text(stringResource(id = R.string.sfw), modifier = Modifier.padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.width(2.dp))

                IconButton(onClick = { performSearch(searchQuery.text, isSfwChecked) }) {
                    Icon(Icons.Filled.Search, contentDescription = stringResource(id = R.string.search))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else if (animeList.isNotEmpty()) {
                AnimeResultsList(animeList = animeList, onItemClick = handleAnimeClick)
            } else if (searchPerformed) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(id = R.string.no_results_to_display))
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AnimeRecommendationsView()
                }
            }

            if (showAnimeDetailDialog && selectedAnimeForDialog != null) {
                val currentAnimeFromApi = selectedAnimeForDialog!!

                val userAnimeEntityFromDb by animeListViewModel.getUserAnimeDetails(currentAnimeFromApi.malId)
                    .collectAsStateWithLifecycle(initialValue = null)

                AnimeDetailDialog(
                    anime = currentAnimeFromApi,
                    initialUserScore = userAnimeEntityFromDb?.userScore,
                    initialStatus = userAnimeEntityFromDb?.status ?: UserAnimeStatus.NONE,
                    onDismissRequest = { showAnimeDetailDialog = false },
                    onStatusChange = { newStatus, newScore ->
                        animeListViewModel.addOrUpdateUserAnime(
                            animeFromApi = currentAnimeFromApi,
                            status = newStatus,
                            userScore = newScore
                        )
                        showAnimeDetailDialog = false
                        Toast.makeText(
                            context,
                            "${currentAnimeFromApi.title} saved as: ${newStatus.name.lowercase().replaceFirstChar { it.titlecase() }}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}

@Composable
fun AnimeResultsList(animeList: List<Anime>, onItemClick: (Anime) -> Unit) { // Zaktualizowano
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(animeList, key = { it.malId }) { anime ->
            AnimeListItem(anime = anime, onItemClick = onItemClick) // Zaktualizowano
        }
    }
}

@Composable
fun AnimeListItem(anime: Anime, onItemClick: (Anime) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(anime) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(anime.images.jpg.imageUrl ?: anime.images.webp.imageUrl)
                    .crossfade(true)
                    .error(android.R.drawable.ic_menu_gallery)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .build(),
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp, 120.dp)
                    .padding(end = 8.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = anime.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2
                )
                anime.titleEnglish?.let {
                    Text(
                        text = "Ang: $it",
                        fontSize = 12.sp,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Typ: ${anime.type ?: "N/A"}",
                    fontSize = 14.sp
                )
                Text(
                    text = "Ocena: ${anime.score?.toString() ?: "N/A"}",
                    fontSize = 14.sp
                )
                anime.episodes?.let {
                    Text(
                        text = "Odcinki: $it",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

