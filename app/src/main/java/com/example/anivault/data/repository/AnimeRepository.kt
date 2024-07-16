    package com.example.anivault.data.repository

    import android.icu.util.Calendar
import com.example.anivault.data.network.JikanApiService
    import com.example.anivault.data.network.response.AnimeDetails
    import com.example.anivault.data.network.response.AnimeRecommendationResponse
import com.example.anivault.data.network.response.AnimeResponse
import com.example.anivault.data.network.response.YearData
import com.example.anivault.ui.dataclassess.Anime
import com.example.anivault.ui.dataclassess.HorizontalAnime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

    class AnimeRepository(private val apiService: JikanApiService) {

        fun getAnimeListCurrentSeason(): Flow<List<Anime>> = getAnimeList { page ->
            apiService.getCurrentSeasonAnime(page)
        }

        fun getAnimeListPreviousSeason(): Flow<List<Anime>> {
            val (prevSeason, prevYear) = getPreviousSeasonInfo()
            return getAnimeList { page ->
                apiService.getPreviousSeasonAnime(prevYear, prevSeason, page)
            }
        }

        fun getAnimeListNextSeason(): Flow<List<Anime>> {
            val (nextSeason, nextYear) = getNextSeasonInfo()
            return getAnimeList { page ->
                apiService.getNextSeasonAnime(nextYear, nextSeason, page)
            }
        }

        fun getAnimeListNextSeasonSearch(): Flow<List<HorizontalAnime>> {
            val (nextSeason, nextYear) = getNextSeasonInfo()
            return getAnimeListHorizontal { page ->
                apiService.getNextSeasonAnime(nextYear, nextSeason, page)
            }
        }

        suspend fun getSeasons(): List<YearData> {
            return apiService.getSeasons().data
        }

        fun getAnimeArchive(year: String, season: String): Flow<List<Anime>> = getAnimeList { page ->
            apiService.getAnimeArchive(year, season, page)
        }


        fun getTopAiringAnime(): Flow<List<HorizontalAnime>> = getTopAiringAnimeList { filter ->
            apiService.getTopAnimeAiring(filter, 1)
        }

        fun getTopAiringAnimeList(apiCall: suspend (String) -> AnimeResponse): Flow<List<HorizontalAnime>> = flow {
            try {
                val response = apiCall("airing")
                val animeList = response.data.map { animeData ->
                    HorizontalAnime(
                        mal_id = animeData.mal_id,
                        title = animeData.title,
                        imageUrl = animeData.images.jpg.image_url
                    )
                }
                emit(animeList)
            } catch (e: Exception) {
                throw e
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
                            mal_id = animeData.mal_id,
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
            val (season, year) = getCurrentSeasonInfo()
            return when (season) {
                "winter" -> Pair("fall", (year.toInt() - 1).toString())
                "spring" -> Pair("winter", year)
                "summer" -> Pair("spring", year)
                else -> Pair("summer", year)
            }
        }

        private fun getNextSeasonInfo(): Pair<String, String> {
            val (season, year) = getCurrentSeasonInfo()
            return when (season) {
                "winter" -> Pair("spring", year)
                "spring" -> Pair("summer", year)
                "summer" -> Pair("fall", year)
                else -> Pair("winter", (year.toInt() + 1).toString())
            }
        }

        private fun getCurrentSeasonInfo(): Pair<String, String> {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR).toString()
            val month = calendar.get(Calendar.MONTH)

            val season = when (month) {
                in 0..2 -> "winter"
                in 3..5 -> "spring"
                in 6..8 -> "summer"
                else -> "fall"
            }

            return Pair(season, year)
        }

        fun getAnimeListHorizontal(apiCall: suspend (Int) -> AnimeResponse): Flow<List<HorizontalAnime>> = flow {
            try {
                val response = apiCall(1)
                val animeList = response.data.map { animeData ->
                    HorizontalAnime(
                        mal_id = animeData.mal_id,
                        title = animeData.title,
                        imageUrl = animeData.images.jpg.image_url
                    )
                }
                emit(animeList)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getAnimeListHorizontalRecommendation(apiCall: suspend (Int) -> AnimeRecommendationResponse): Flow<List<HorizontalAnime>> = flow {
            try {
                val response = apiCall(1)
                val animeList = response.data.flatMap { recommendationEntry ->
                    recommendationEntry.entry.map { animeEntry ->
                        HorizontalAnime(
                            mal_id = animeEntry.mal_id,
                            title = animeEntry.title,
                            imageUrl = animeEntry.images.jpg.image_url
                        )
                    }
                }
                emit(animeList)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getTopAnimeHorizontal(): Flow<List<HorizontalAnime>> = getAnimeListHorizontal { page ->
            apiService.getTopAnime(page)
        }

        fun getRecommendationAnimeHorizontal(): Flow<List<HorizontalAnime>> = getAnimeListHorizontalRecommendation { page ->
            apiService.getRecommendationAnime(page)
        }

        fun getAnimeDetailsFlow(animeId: Int): Flow<AnimeDetails> = flow {
            val response = apiService.getAnimeDetails(animeId)
            emit(response.data)
        }

    }
