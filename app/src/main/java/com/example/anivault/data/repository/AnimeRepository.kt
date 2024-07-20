    package com.example.anivault.data.repository

    import android.icu.util.Calendar
import com.example.anivault.data.network.JikanApiService
import com.example.anivault.data.network.response.AnimeDetails
import com.example.anivault.data.network.response.YearData
import com.example.anivault.ui.dataclassess.Anime
import com.example.anivault.ui.dataclassess.HorizontalAnime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

    class AnimeRepository(private val apiService: JikanApiService) {
        private val requestSemaphore = Semaphore(1) // Allow only one request at a time
        private val cache = mutableMapOf<String, CacheEntry>()
        private val cacheTimeout = TimeUnit.MINUTES.toMillis(5) // 5 minutes cache timeout

        private data class CacheEntry(val data: Any, val timestamp: Long)

        private suspend fun <T> makeApiCall(key: String, apiCall: suspend () -> T): T {
            // Check cache first
            cache[key]?.let {
                if (System.currentTimeMillis() - it.timestamp < cacheTimeout) {
                    return it.data as T
                }
            }

            return requestSemaphore.withPermit {
                try {
                    val result = apiCall()
                    cache[key] = CacheEntry(result as Any, System.currentTimeMillis())
                    result
                } catch (e: HttpException) {
                    if (e.code() == 429) {
                        // Implement exponential backoff
                        delay(5000) // Wait for 5 seconds before retrying
                        makeApiCall(key, apiCall) // Retry the call
                    } else {
                        throw e
                    }
                }
            }
        }

        fun getAnimeListCurrentSeason(): Flow<List<Anime>> = flow {
            var currentPage = 1
            var hasNextPage = true

            while (hasNextPage) {
                val response = makeApiCall("current_season_$currentPage") {
                    apiService.getCurrentSeasonAnime(currentPage)
                }
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
                delay(1000) // Delay to respect rate limits
            }
        }

        fun getAnimeListPreviousSeason(): Flow<List<Anime>> = flow {
            val (prevSeason, prevYear) = getPreviousSeasonInfo()
            var currentPage = 1
            var hasNextPage = true

            while (hasNextPage) {
                val response = makeApiCall("previous_season_${prevYear}_${prevSeason}_$currentPage") {
                    apiService.getPreviousSeasonAnime(prevYear, prevSeason, currentPage)
                }
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
                delay(1000) // Delay to respect rate limits
            }
        }

        fun getAnimeListNextSeason(): Flow<List<Anime>> = flow {
            val (nextSeason, nextYear) = getNextSeasonInfo()
            var currentPage = 1
            var hasNextPage = true

            while (hasNextPage) {
                val response = makeApiCall("next_season_${nextYear}_${nextSeason}_$currentPage") {
                    apiService.getNextSeasonAnime(nextYear, nextSeason, currentPage)
                }
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
                delay(1000) // Delay to respect rate limits
            }
        }

        fun getAnimeListNextSeasonSearch(): Flow<List<HorizontalAnime>> = flow {
            val (nextSeason, nextYear) = getNextSeasonInfo()
            val response = makeApiCall("next_season_search_${nextYear}_$nextSeason") {
                apiService.getNextSeasonAnime(nextYear, nextSeason, 1)
            }
            val animeList = response.data.map { animeData ->
                HorizontalAnime(
                    mal_id = animeData.mal_id,
                    title = animeData.title,
                    imageUrl = animeData.images.jpg.image_url
                )
            }
            emit(animeList)
        }

        suspend fun getSeasons(): List<YearData> {
            return makeApiCall("seasons") {
                apiService.getSeasons().data
            }
        }

        fun getAnimeArchive(year: String, season: String): Flow<List<Anime>> = flow {
            var currentPage = 1
            var hasNextPage = true

            while (hasNextPage) {
                val response = makeApiCall("archive_${year}_${season}_$currentPage") {
                    apiService.getAnimeArchive(year, season, currentPage)
                }
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
                delay(1000) // Delay to respect rate limits
            }
        }

        fun getTopAiringAnime(): Flow<List<HorizontalAnime>> = flow {
            val response = makeApiCall("top_airing") {
                apiService.getTopAnimeAiring("airing", 1)
            }
            val animeList = response.data.map { animeData ->
                HorizontalAnime(
                    mal_id = animeData.mal_id,
                    title = animeData.title,
                    imageUrl = animeData.images.jpg.image_url
                )
            }
            emit(animeList)
        }

        fun getTopAnimeHorizontal(): Flow<List<HorizontalAnime>> = flow {
            val response = makeApiCall("top_anime") {
                apiService.getTopAnime(1)
            }
            val animeList = response.data.map { animeData ->
                HorizontalAnime(
                    mal_id = animeData.mal_id,
                    title = animeData.title,
                    imageUrl = animeData.images.jpg.image_url
                )
            }
            emit(animeList)
        }

        fun getRecommendationAnimeHorizontal(): Flow<List<HorizontalAnime>> = flow {
            val response = makeApiCall("recommendations") {
                apiService.getRecommendationAnime(1)
            }
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
        }

        fun getAnimeDetailsFlow(animeId: Int): Flow<AnimeDetails> = flow {
            val response = makeApiCall("anime_details_$animeId") {
                apiService.getAnimeDetails(animeId)
            }
            emit(response.data)
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
    }