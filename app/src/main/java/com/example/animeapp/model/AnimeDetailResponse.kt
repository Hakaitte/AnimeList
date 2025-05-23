package com.example.animeapp.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeDetailResponse(
    val data: Anime? = null,
    val status: Int? = null,
    val type: String? = null,
    val message: String? = null,
    val error: String? = null
)
