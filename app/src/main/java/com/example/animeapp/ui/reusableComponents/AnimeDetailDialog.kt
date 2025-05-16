package com.example.animeapp.ui.reusableComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.animeapp.model.Anime

@Composable
fun AnimeDetailDialog(
    anime: Anime,
    onDismissRequest: () -> Unit,
    onAddToWatching: () -> Unit,
    onAddToPlanning: () -> Unit,
    onAddToCompleted: () -> Unit,
    onAddToOnHold: () -> Unit,
    onAddToDropped: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = anime.title,
                style = MaterialTheme.typography.headlineSmall, // Użyj stylu z tematu
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
        },
        text = {
            // Użyj LazyColumn, jeśli zawartość może być długa i wymagać przewijania
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            // Spróbuj najpierw duży obrazek, potem standardowy
                            .data(anime.images.jpg.largeImageUrl ?: anime.images.jpg.imageUrl ?: anime.images.webp.largeImageUrl ?: anime.images.webp.imageUrl)
                            .crossfade(true)
                            .error(android.R.drawable.stat_notify_error) // Lepsza ikona błędu
                            .placeholder(android.R.drawable.stat_sys_download) // Lepszy placeholder ładowania
                            .build(),
                        contentDescription = "Plakat ${anime.title}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp, max = 300.dp) // Min i max wysokość
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(12.dp)) // Większe zaokrąglenie
                    )
                }

                // Sekcja z przyciskami
                item {
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp) // Odstępy między przyciskami
                    ) {
                        DialogButton(text = "Oglądam", onClick = onAddToWatching)
                        DialogButton(text = "Planuję obejrzeć", onClick = onAddToPlanning)
                        DialogButton(text = "Obejrzane", onClick = onAddToCompleted)
                        DialogButton(text = "Wstrzymane", onClick = onAddToOnHold)
                        DialogButton(text = "Porzucone", onClick = onAddToDropped)
                    }
                }
            }
        },
        confirmButton = {
            // Możemy użyć tego do głównego przycisku "Zamknij", jeśli nie chcemy go na dole.
            // Lub zostawić puste, jeśli przyciski akcji są w 'text'.
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Anuluj")
            }
        },
        modifier = Modifier.padding(16.dp) // Dodaj padding do całego dialogu
    )
}

// Pomocniczy Composable dla przycisków w dialogu, aby uniknąć powtórzeń
@Composable
private fun DialogButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp) // Zaokrąglone rogi dla przycisków
    ) {
        Text(text)
    }
}