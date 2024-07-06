package com.example.anivault.data.network

import com.example.anivault.data.network.response.AnimeResponse
import com.example.anivault.data.network.response.SeasonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {
    @GET("seasons/now")
    suspend fun getCurrentSeasonAnime(@Query("page") page: Int): AnimeResponse

    @GET("seasons/{year}/{season}")
    suspend fun getNextSeasonAnime(
        @Path("year") year: String,
        @Path("season") season: String,
        @Query("page") page: Int
    ): AnimeResponse

    @GET("seasons/{year}/{season}")
    suspend fun getPreviousSeasonAnime(
        @Path("year") year: String,
        @Path("season") season: String,
        @Query("page") page: Int
    ): AnimeResponse

    @GET("seasons/{year}/{season}")
    suspend fun getAnimeArchive(
        @Path("year") year: String,
        @Path("season") season: String,
        @Query("page") page: Int
    ): AnimeResponse

    @GET("seasons")
    suspend fun getSeasons(): SeasonResponse


}
