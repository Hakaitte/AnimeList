package com.example.animeapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pagination(
    @Json(name = "last_visible_page") val lastVisiblePage: Int,
    @Json(name = "has_next_page") val hasNextPage: Boolean,
)