package com.example.animeapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeRecommendationsResponse(
    @Json(name = "pagination") val pagination: Pagination,
    @Json(name = "data") val data: List<Recommendation>
)

@JsonClass(generateAdapter = true)
data class Recommendation(
    @Json(name = "mal_id") val malId: String,
    @Json(name = "entry") val entry: List<AnimeEntry>,
    @Json(name = "content") val content: String,
    @Json(name = "date") val date: String,
    @Json(name = "user") val user: User
)

@JsonClass(generateAdapter = true)
data class AnimeEntry(
    @Json(name = "mal_id") val malId: Int,
    @Json(name = "url") val url: String,
    @Json(name = "images") val images: Images,
    @Json(name = "title") val title: String
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "url") val url: String,
    @Json(name = "username") val username: String
)