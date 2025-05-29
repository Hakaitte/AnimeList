package com.example.animeapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.animeapp.AnimeListViewModel
import com.example.animeapp.R
import com.example.animeapp.data.UserAnimeEntity
import com.example.animeapp.data.UserAnimeStatus
import com.example.animeapp.ui.reusableComponents.StarRatingInput
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListScreen(
    navController: NavController,
    status: UserAnimeStatus,
    animeListViewModel: AnimeListViewModel = viewModel()
) {
    val animeListFlow = animeListViewModel.getAnimeByStatus(status)
    val animeList by animeListFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    var selectedAnimeId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.list_title, status.displayNameRes())) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(animeList, key = { it.malId }) { anime ->
                AnimeListItem(
                    anime = anime,
                    onUpdateScore = { newScore ->
                        animeListViewModel.updateUserAnimeStatusAndScore(anime.malId, anime.status, newScore)
                    },
                    onRemove = {
                        animeListViewModel.removeAnimeFromList(anime.malId)
                    },
                    onUpdateStatus = { newStatus ->
                        animeListViewModel.updateUserAnimeStatusAndScore(anime.malId, newStatus, anime.userScore)
                    },
                    onAnimeClick = { selectedAnimeId = anime.malId }
                )
            }
        }
        selectedAnimeId?.let { animeId ->
            com.example.animeapp.ui.reusableComponents.AnimeDescriptionDialog(
                animeId = animeId,
                onDismissRequest = { selectedAnimeId = null },
                showAddToListButton = false
            )
        }
    }
}

@Composable
fun AnimeListItem(
    anime: UserAnimeEntity,
    onUpdateScore: (Int) -> Unit,
    onRemove: () -> Unit,
    onUpdateStatus: (UserAnimeStatus) -> Unit,
    onAnimeClick: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var newScore by remember { mutableStateOf(anime.userScore ?: 0) }
    var showStatusMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onAnimeClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(anime.title, style = MaterialTheme.typography.titleLarge)
            anime.imageUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp, 120.dp).padding(end = 8.dp)
                )
            }
            val formattedScore = anime.userScore?.let { score
                ->
                val dividedScore = score / 2.0
                String.format(java.util.Locale.US, "%.1f", dividedScore)
            } ?: "None"
            Text(stringResource(id = R.string.score, formattedScore))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(id = R.string.status, anime.status.displayNameRes()))
                Spacer(Modifier.size(8.dp))
            }
            val formattedDate: String? = anime.addedDate.let { millis ->
                val instant = Instant.ofEpochMilli(millis)
                val strafe = ZoneId.of("Europe/Warsaw")
                val localDateTime = LocalDateTime.ofInstant(instant, strafe)
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss")
                localDateTime.format(formatter)
            }
            Text(stringResource(id = R.string.modification_date, formattedDate ?: ""))
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Button(onClick = { showEditDialog = true }, modifier = Modifier.weight(1f)) {
                    Text(stringResource(id = R.string.change_score))
                }
                Spacer(modifier = Modifier.size(8.dp))
                Box {
                    Button(onClick = { showStatusMenu = true }) {
                        Text(stringResource(id = R.string.change_status))
                    }
                    DropdownMenu(
                        expanded = showStatusMenu,
                        onDismissRequest = { showStatusMenu = false }
                    ) {
                        UserAnimeStatus.values().filter { it != anime.status && it != UserAnimeStatus.NONE }.forEach { statusOption ->
                            DropdownMenuItem(
                                text = { Text(statusOption.displayNameRes()) },
                                onClick = {
                                    onUpdateStatus(statusOption)
                                    showStatusMenu = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text(stringResource(id = R.string.change_score_for, anime.title)) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(id = R.string.new_score))
                    StarRatingInput(
                        rating = newScore,
                        onRatingChange = { value -> newScore = value },
                        starSize = 38.dp
                    )
                    Text(stringResource(id = R.string.score_out_of, newScore / 2.0))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onUpdateScore(newScore)
                    showEditDialog = false
                }) { Text(stringResource(id = R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text(stringResource(id = R.string.cancel)) }
            }
        )
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(id = R.string.confirm_delete)) },
            text = { Text(stringResource(id = R.string.confirm_delete_text)) },
            confirmButton = {
                TextButton(onClick = {
                    onRemove()
                    showDeleteDialog = false
                }) { Text(stringResource(id = R.string.delete)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text(stringResource(id = R.string.cancel)) }
            }
        )
    }
}

fun AnimeListViewModel.getAnimeByStatus(status: UserAnimeStatus) = when (status) {
    UserAnimeStatus.WATCHING -> getWatchingAnime()
    UserAnimeStatus.PLAN_TO_WATCH -> getPlannedAnime()
    UserAnimeStatus.COMPLETED -> getCompletedAnime()
    UserAnimeStatus.ON_HOLD -> getOnHoldAnime()
    UserAnimeStatus.DROPPED -> getDroppedAnime()
    else -> getWatchingAnime()
}

fun UserAnimeStatus.displayNameRes(): String = when(this) {
    UserAnimeStatus.WATCHING -> "Watching"
    UserAnimeStatus.PLAN_TO_WATCH -> "Plan to Watch"
    UserAnimeStatus.COMPLETED -> "Completed"
    UserAnimeStatus.ON_HOLD -> "On Hold"
    UserAnimeStatus.DROPPED -> "Dropped"
    else -> R.string.unknown
}.toString()
