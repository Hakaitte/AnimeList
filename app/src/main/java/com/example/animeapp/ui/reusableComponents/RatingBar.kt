package com.example.animeapp.ui.reusableComponents

// Alternatywnie: import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.animeapp.ui.theme.AnimeAppTheme

@Composable
fun StarRatingInput(
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    rating: Int, // Aktualna ocena 0-10
    onRatingChange: (Int) -> Unit,
    starSize: Dp = 38.dp, // Zwiększono dla lepszej klikalności
    filledColor: Color = MaterialTheme.colorScheme.primary,
    emptyColor: Color = Color.LightGray
) {
    Row(modifier = modifier) {
        for (starIndex in 1..maxStars) {
            // Wartość punktowa dla tej gwiazdki, jeśli kliknięto lewą połówkę
            val leftHalfValue = (starIndex - 1) * 2 + 1
            // Wartość punktowa dla tej gwiazdki, jeśli kliknięto prawą połówkę (lub całą)
            val rightHalfValue = starIndex * 2

            Box { // Kontener dla jednej gwiazdki, aby obsłużyć dwa klikalne obszary
                // Ikona bazowa (pusta lub częściowo wypełniona)
                Icon(
                    imageVector = when {
                        rating >= rightHalfValue -> Icons.Filled.Star       // Pełna gwiazdka
                        rating >= leftHalfValue -> Icons.Filled.StarHalf  // Połowa gwiazdki
                        else -> Icons.Outlined.Star                       // Pusta gwiazdka
                    },
                    contentDescription = "Gwiazdka $starIndex",
                    tint = if (rating >= leftHalfValue) filledColor else emptyColor,
                    modifier = Modifier.size(starSize)
                )
                // Niewidoczne klikalne obszary nad ikoną
                Row {
                    Spacer( // Lewa połówka klikalna
                        modifier = Modifier
                            .size(starSize / 2, starSize)
                            .clickable { onRatingChange(leftHalfValue) }
                    )
                    Spacer( // Prawa połówka klikalna
                        modifier = Modifier
                            .size(starSize / 2, starSize)
                            .clickable { onRatingChange(rightHalfValue) }
                    )
                }
            }
            if (starIndex < maxStars) {
                Spacer(modifier = Modifier.width(4.dp)) // Odstęp między gwiazdkami
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StarRatingInputPreview() {
    var currentRating by remember { mutableStateOf(0) } // 0-10
    AnimeAppTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
            Text("Twoja ocena: ${currentRating / 2.0} / 5.0 (${currentRating} pkt)", modifier = Modifier.padding(bottom = 8.dp))

            StarRatingInput(
                rating = currentRating,
                onRatingChange = { newRating ->
                    currentRating = newRating
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            StarRatingInput(
                rating = 7, // 3.5 gwiazdki
                onRatingChange = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            StarRatingInput(
                rating = 10, // 5 gwiazdek
                onRatingChange = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            StarRatingInput(
                rating = 3, // 1.5 gwiazdki
                onRatingChange = {}
            )
        }
    }
}
