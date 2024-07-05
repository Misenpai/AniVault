package com.example.anivault.data.network

import retrofit2.Response
import retrofit2.http.GET

interface JikanApiService {
    @GET("seasons/now")
    suspend fun getCurrentSeasonAnime(): Response<AnimeResponse>
}