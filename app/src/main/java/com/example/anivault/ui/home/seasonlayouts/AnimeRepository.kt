package com.example.anivault.ui.home.seasonlayouts

import com.example.anivault.data.network.JikanApiService
import com.example.anivault.ui.dataclassess.Anime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnimeRepository(private val apiService: JikanApiService) {
    suspend fun getAnimeList(): List<Anime> {
        return withContext(Dispatchers.IO) {
            val response = apiService.getCurrentSeasonAnime()
            if (response.isSuccessful) {
                response.body()?.data?.map { animeResponse ->
                    Anime(
                        imageUrl = animeResponse.images.jpg.image_url,
                        title = animeResponse.title,
                        genres = animeResponse.genres.map { it.name }
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        }
    }
}
