package com.misenpai.shared.data.network

import com.misenpai.anivault.data.network.response.AnimeDetailsResponse
import com.misenpai.anivault.data.network.response.AnimeRecommendationResponse
import com.misenpai.anivault.data.network.response.AnimeResponse
import com.misenpai.anivault.data.network.response.AnimeSearchResponse
import com.misenpai.anivault.data.network.response.SeasonResponse
import retrofit2.Response
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

    @GET("top/anime")
    suspend fun getTopAnime(@Query("page") page: Int): AnimeResponse

    @GET("recommendations/anime")
    suspend fun getRecommendationAnime(@Query("page") page: Int): AnimeRecommendationResponse

    @GET("top/anime")
    suspend fun getTopAnimeAiring(@Query("filter") filter: String,@Query("page") page: Int): AnimeResponse

    @GET("anime/{id}/full")
    suspend fun getAnimeDetails(@Path("id") id: Int): AnimeDetailsResponse

    @GET("anime")
    suspend fun searchAnime(@Query("q") query: String): Response<AnimeSearchResponse>

}
