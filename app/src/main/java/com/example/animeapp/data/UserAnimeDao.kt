package com.example.animeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Jeśli anime już istnieje, zaktualizuj je
    suspend fun insertOrUpdateAnime(anime: UserAnimeEntity)

    @Query("SELECT * FROM user_anime_list WHERE malId = :malId")
    fun getAnimeById(malId: Int): Flow<UserAnimeEntity?> // Flow do obserwacji zmian

    @Query("SELECT * FROM user_anime_list WHERE status = :status ORDER BY title ASC")
    fun getAnimeByStatus(status: UserAnimeStatus): Flow<List<UserAnimeEntity>>

    @Query("SELECT * FROM user_anime_list ORDER BY title ASC")
    fun getAllAnime(): Flow<List<UserAnimeEntity>>

    @Query("SELECT * FROM user_anime_list WHERE malId = :malId")
    suspend fun getAnimeByIdOnce(malId: Int): UserAnimeEntity?

    @Query("DELETE FROM user_anime_list WHERE malId = :malId")
    suspend fun deleteAnimeById(malId: Int)

    @Query("UPDATE user_anime_list SET status = :newStatus, userScore = :newUserScore, addedDate = :timestamp WHERE malId = :malId")
    suspend fun updateAnimeStatusAndScore(malId: Int, newStatus: UserAnimeStatus, newUserScore: Int?, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(malId) FROM user_anime_list WHERE status = :status")
    fun getAnimeCountByStatus(status: UserAnimeStatus): Flow<Int>
}