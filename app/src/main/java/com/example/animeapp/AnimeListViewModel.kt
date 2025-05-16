package com.example.animeapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.data.AppDatabase
import com.example.animeapp.data.UserAnimeEntity
import com.example.animeapp.data.UserAnimeStatus
import com.example.animeapp.model.Anime // Twój model z API
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AnimeListViewModel(application: Application) : AndroidViewModel(application) {

    private val userAnimeDao = AppDatabase.getDatabase(application).userAnimeDao()

    // Funkcja do pobierania statusu i oceny dla konkretnego anime
    fun getUserAnimeDetails(malId: Int): Flow<UserAnimeEntity?> {
        return userAnimeDao.getAnimeById(malId)
    }

    fun addOrUpdateUserAnime(
        animeFromApi: Anime,
        status: UserAnimeStatus,
        userScore: Int? = null
    ) {
        viewModelScope.launch {
            val existingEntity = userAnimeDao.getAnimeByIdOnce(animeFromApi.malId) // Użyj nowej metody

            val entity = UserAnimeEntity(
                malId = animeFromApi.malId,
                title = animeFromApi.title,
                imageUrl = animeFromApi.images.jpg.largeImageUrl
                    ?: animeFromApi.images.jpg.imageUrl
                    ?: animeFromApi.images.webp.largeImageUrl
                    ?: animeFromApi.images.webp.imageUrl,
                status = status,
                userScore = userScore ?: existingEntity?.userScore, // Zachowaj starą ocenę
                // Jeśli existingEntity istnieje, możesz chcieć zachować jego addedDate
                // lub episodesWatched, jeśli je implementujesz i nie są aktualizowane
                addedDate = existingEntity?.addedDate ?: System.currentTimeMillis()
            )
            userAnimeDao.insertOrUpdateAnime(entity)
        }
    }

    fun updateUserAnimeStatusAndScore(
        malId: Int,
        newStatus: UserAnimeStatus,
        newUserScore: Int?
    ) {
        viewModelScope.launch {
            userAnimeDao.updateAnimeStatusAndScore(malId, newStatus, newUserScore)
        }
    }

    fun removeAnimeFromList(malId: Int) {
        viewModelScope.launch {
            userAnimeDao.deleteAnimeById(malId)
        }
    }

    fun getWatchingAnime(): Flow<List<UserAnimeEntity>> = userAnimeDao.getAnimeByStatus(UserAnimeStatus.WATCHING)
    fun getPlannedAnime(): Flow<List<UserAnimeEntity>> = userAnimeDao.getAnimeByStatus(UserAnimeStatus.PLAN_TO_WATCH)
    fun getCompletedAnime(): Flow<List<UserAnimeEntity>> = userAnimeDao.getAnimeByStatus(UserAnimeStatus.COMPLETED)
    fun getDroppedAnime(): Flow<List<UserAnimeEntity>> = userAnimeDao.getAnimeByStatus(UserAnimeStatus.DROPPED)
    fun getOnHoldAnime(): Flow<List<UserAnimeEntity>> = userAnimeDao.getAnimeByStatus(UserAnimeStatus.ON_HOLD)
    fun getAnimeCountByStatus(status: UserAnimeStatus): Flow<Int> {
        return userAnimeDao.getAnimeCountByStatus(status)
    }
}