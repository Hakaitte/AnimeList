package com.example.animeapp.retrofit

import com.example.animeapp.model.AnimeDetailResponse
import com.example.animeapp.model.AnimeResponse
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object RetrofitClient {

    private const val BASE_URL = "https://api.jikan.moe/v4/"

    // Function to build and return a Retrofit instance
    private fun getClient(): Retrofit {
        // Create a Moshi instance for JSON parsing KotlinJsonAdapterFactory is used to
        // automatically generate the adapter for the data classes
        val moshi = Moshi.Builder().build()
        // Create a Retrofit instance with the BASE_URL and Moshi converter
        // using the builder pattern
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        // Return the Retrofit instance
        return retrofit
    }

    val apiServiceInstance: ApiService by lazy {
        // Create a Retrofit instance using the getClient() method to access the API
        // via the ApiService interface
        getClient().create(ApiService::class.java)
    }
}

interface ApiService {
    @GET("anime")
    fun searchAnime(
        @Query("q") query: String,
        @Query("sfw") safeForWork: Boolean,
        @Query("limit") limit: Int = 25,
        @Query("order_by") orderBy: String = "rank"
    ): Call<AnimeResponse>

    @GET("anime/{id}")
    fun searchAnimeById(
        @Path("id") id: Int
    ): Call<AnimeDetailResponse>
}