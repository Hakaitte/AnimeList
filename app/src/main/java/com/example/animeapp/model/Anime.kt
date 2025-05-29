package com.example.animeapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass



@JsonClass(generateAdapter = true)
data class Items(
    val count: Int,
    val total: Int,
    @Json(name = "per_page") val perPage: Int
)

@JsonClass(generateAdapter = true)
data class Anime(
    @Json(name = "mal_id") val malId: Int,
    val url: String,
    val images: Images,
    val trailer: Trailer,
    val approved: Boolean,
    val titles: List<Title>,
    val title: String,
    @Json(name = "title_english") val titleEnglish: String?,
    @Json(name = "title_japanese") val titleJapanese: String?,
    @Json(name = "title_synonyms") val titleSynonyms: List<String>,
    val type: String?,
    val source: String?,
    val episodes: Int?,
    val status: String?,
    val airing: Boolean,
    val aired: Aired,
    val duration: String?,
    val rating: String?,
    val score: Double,
    @Json(name = "scored_by") val scoredBy: Int?,
    val rank: Int?,
    val popularity: Int?,
    val members: Int?,
    val favorites: Int?,
    val synopsis: String?,
    val background: String?,
    val season: String?,
    val year: Int?,
    val broadcast: Broadcast?,
    val producers: List<Entity>,
    val licensors: List<Entity>,
    val studios: List<Entity>,
    val genres: List<Entity>,
    @Json(name = "explicit_genres") val explicitGenres: List<Entity>,
    val themes: List<Entity>,
    val demographics: List<Entity>
)

@JsonClass(generateAdapter = true)
data class Images(
    val jpg: ImageUrls,
    val webp: ImageUrls
)

@JsonClass(generateAdapter = true)
data class ImageUrls(
    @Json(name = "image_url") val imageUrl: String?,
    @Json(name = "small_image_url") val smallImageUrl: String?,
    @Json(name = "large_image_url") val largeImageUrl: String?
)

@JsonClass(generateAdapter = true)
data class Trailer(
    @Json(name = "youtube_id") val youtubeId: String?,
    val url: String?,
    @Json(name = "embed_url") val embedUrl: String?,
    val images: TrailerImages
)

@JsonClass(generateAdapter = true)
data class TrailerImages(
    @Json(name = "image_url") val imageUrl: String?,
    @Json(name = "small_image_url") val smallImageUrl: String?,
    @Json(name = "medium_image_url") val mediumImageUrl: String?,
    @Json(name = "large_image_url") val largeImageUrl: String?,
    @Json(name = "maximum_image_url") val maximumImageUrl: String?
)

@JsonClass(generateAdapter = true)
data class Title(
    val type: String,
    val title: String
)

@JsonClass(generateAdapter = true)
data class Aired(
    val from: String?,
    val to: String?,
    val prop: AiredProp,
    val string: String?
)

@JsonClass(generateAdapter = true)
data class AiredProp(
    val from: AiredDate,
    val to: AiredDate
)

@JsonClass(generateAdapter = true)
data class AiredDate(
    val day: Int?,
    val month: Int?,
    val year: Int?
)

@JsonClass(generateAdapter = true)
data class Broadcast(
    val day: String?,
    val time: String?,
    val timezone: String?,
    val string: String?
)

@JsonClass(generateAdapter = true)
data class Entity(
    @Json(name = "mal_id") val malId: Int,
    val type: String,
    val name: String,
    val url: String
)

