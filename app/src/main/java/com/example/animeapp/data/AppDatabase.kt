package com.example.animeapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

class Converters {
    @androidx.room.TypeConverter
    fun fromUserAnimeStatus(value: UserAnimeStatus?): String? {
        return value?.name
    }

    @androidx.room.TypeConverter
    fun toUserAnimeStatus(value: String?): UserAnimeStatus? {
        return value?.let { UserAnimeStatus.valueOf(it) }
    }
}


@Database(entities = [UserAnimeEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userAnimeDao(): UserAnimeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "anime_app_database"
                )
                    // W przyszłości można dodać migracje: .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}