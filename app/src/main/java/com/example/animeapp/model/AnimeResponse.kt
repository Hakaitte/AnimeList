package com.example.animeapp.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeResponse(
    val pagination: Pagination? = null,
    val data: List<Anime>? = null,
    val status: Int? = null,
    val type: String? = null,
    val message: String? = null,
    val error: String? = null
)