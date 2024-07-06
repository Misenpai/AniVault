package com.example.anivault.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {
    @GET("seasons/now")
    suspend fun getCurrentSeasonAnime(@Query("page") page: Int): AnimeResponse

    @GET("seasons/upcoming")
    suspend fun getNextSeasonAnime(@Query("page") page: Int) : AnimeResponse

    @GET("seasons/{year}/{season}")
    suspend fun getSeasonAnime(
        @Path("year") year: String,
        @Path("season") season: String,
        @Query("page") page: Int
    ): AnimeResponse


}
