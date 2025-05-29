package com.example.animeapp.ui.reusableComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.animeapp.data.UserAnimeStatus
import com.example.animeapp.model.Anime

@Composable
fun AnimeDetailDialog(
    anime: Anime,
    initialUserScore: Int?,
    initialStatus: UserAnimeStatus,
    onDismissRequest: () -> Unit,
    onStatusChange: (newStatus: UserAnimeStatus, newScore: Int?) -> Unit,
) {
    var selectedUiStatus by remember { mutableStateOf(initialStatus) }
    var currentRatingInDialog by remember { mutableStateOf(initialUserScore ?: 0) }
    var showDescriptionDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
        },
        text = {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                item(key = "dialog_image_${anime.malId}") {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(anime.images.jpg.largeImageUrl ?: anime.images.jpg.imageUrl ?: anime.images.webp.largeImageUrl ?: anime.images.webp.imageUrl)
                            .crossfade(true)
                            .error(android.R.drawable.stat_notify_error)
                            .placeholder(android.R.drawable.stat_sys_download)
                            .build(),
                        contentDescription = "Poster ${anime.title}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp, max = 270.dp)
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showDescriptionDialog = true }
                    )
                }

                item(key = "dialog_user_rating_stars_${anime.malId}") {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Twoja ocena: ${if (currentRatingInDialog > 0) "${currentRatingInDialog / 2.0} / 5.0" else "None"}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        StarRatingInput(
                            rating = currentRatingInDialog,
                            onRatingChange = { newRating ->
                                currentRatingInDialog = newRating
                            },
                            starSize = 32.dp
                        )
                    }
                }

                item(key = "dialog_status_buttons_${anime.malId}") {
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val statuses = UserAnimeStatus.values().filter { it != UserAnimeStatus.NONE }
                        statuses.forEach { status ->
                            DialogButton(
                                text = status.name.replace("_", " ").lowercase()
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                                onClick = {
                                    // TUTAJ AKTUALIZUJEMY STAN WYBRANEGO PRZYCISKU W UI
                                    selectedUiStatus = status
                                },
                                // TUTAJ PRZEKAZUJEMY, CZY PRZYCISK MA BYĆ PODŚWIETLONY
                                isSelected = selectedUiStatus == status
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (currentRatingInDialog == 0) null else currentRatingInDialog
                    if (selectedUiStatus != UserAnimeStatus.NONE || (initialStatus != UserAnimeStatus.NONE && currentRatingInDialog != initialUserScore)) {
                        val statusToSave = selectedUiStatus // Zawsze używaj tego, co jest wybrane w UI

                        if(statusToSave != UserAnimeStatus.NONE || currentRatingInDialog != null){
                            onStatusChange(statusToSave, currentRatingInDialog)
                        } else if (statusToSave == UserAnimeStatus.NONE && initialStatus != UserAnimeStatus.NONE && currentRatingInDialog == null && initialUserScore == null) {
                            onStatusChange(statusToSave, currentRatingInDialog) // Przykład: Pozwól zapisać NONE, jeśli tak zdecydujesz
                        }
                    } else if (initialStatus == UserAnimeStatus.NONE && selectedUiStatus != UserAnimeStatus.NONE) {
                        onStatusChange(selectedUiStatus, currentRatingInDialog)
                    }
                    onDismissRequest()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        modifier = Modifier.padding(16.dp)
    )
    if (showDescriptionDialog) {
        AnimeDescriptionDialog(
            animeId = anime.malId,
            onDismissRequest = { showDescriptionDialog = false }
        )
    }
}

@Composable
private fun DialogButton(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = if (isSelected) {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            ButtonDefaults.buttonColors(
                // Możesz użyć secondaryContainer lub surfaceVariant dla mniej wyróżniających się przycisków
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    ) {
        Text(text)
    }
}