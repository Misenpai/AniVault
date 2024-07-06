package com.example.anivault.data.repository

import android.icu.util.Calendar
import com.example.anivault.data.network.AnimeResponse
import com.example.anivault.data.network.JikanApiService
import com.example.anivault.ui.dataclassess.Anime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class AnimeRepository(private val apiService: JikanApiService) {

    fun getAnimeListCurrentSeason(): Flow<List<Anime>> = getAnimeList { page ->
        apiService.getCurrentSeasonAnime(page)
    }

    fun getAnimeListNextSeason(): Flow<List<Anime>> = getAnimeList { page ->
        apiService.getNextSeasonAnime(page)
    }

    fun getAnimeListPreviousSeason(): Flow<List<Anime>> {
        val (prevSeason, prevYear) = getPreviousSeasonInfo()
        return getAnimeList { page ->
            apiService.getSeasonAnime(prevYear, prevSeason, page)
        }
    }

    private fun getAnimeList(apiCall: suspend (Int) -> AnimeResponse): Flow<List<Anime>> = flow {
        var currentPage = 1
        var hasNextPage = true

        while (hasNextPage) {
            try {
                val response = apiCall(currentPage)
                val animeList = response.data.map { animeData ->
                    Anime(
                        imageUrl = animeData.images.jpg.image_url,
                        title = animeData.title,
                        genres = animeData.genres.map { it.name }
                    )
                }
                emit(animeList)

                hasNextPage = response.pagination.has_next_page
                currentPage++
                delay(5000) // Wait for 5 seconds before making the next request
            } catch (e: HttpException) {
                hasNextPage = false
                throw e
            }
        }
    }

    private fun getPreviousSeasonInfo(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val currentSeason = when (month) {
            in 0..2 -> "winter"
            in 3..5 -> "spring"
            in 6..8 -> "summer"
            else -> "fall"
        }

        val previousSeason = when (currentSeason) {
            "winter" -> {
                year--
                "fall"
            }
            "spring" -> "winter"
            "summer" -> "spring"
            else -> "summer"
        }

        return Pair(previousSeason, year.toString())
    }
}
