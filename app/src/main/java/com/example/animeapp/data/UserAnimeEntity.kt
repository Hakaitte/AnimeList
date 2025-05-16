package com.example.animeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserAnimeStatus {
    WATCHING,       // Oglądam
    PLAN_TO_WATCH,  // Planuję obejrzeć
    COMPLETED,      // Obejrzane
    ON_HOLD,        // Wstrzymane
    DROPPED,        // Porzucone
    NONE            // Nie ma na żadnej liście (może być przydatne)
}

@Entity(tableName = "user_anime_list")
data class UserAnimeEntity(
    @PrimaryKey val malId: Int, // To samo ID co z API, będzie kluczem głównym
    val title: String,
    val imageUrl: String?, // Możemy przechowywać URL obrazka dla szybszego dostępu
    var status: UserAnimeStatus,
    var userScore: Int? = null, // Ocena użytkownika (np. 1-10), null jeśli nieocenione
    val episodesWatched: Int? = null, // Opcjonalnie: ile odcinków obejrzano
    val addedDate: Long = System.currentTimeMillis() // Data dodania/modyfikacji
)