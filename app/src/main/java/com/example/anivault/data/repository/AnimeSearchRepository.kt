package com.example.anivault.data.repository

import com.example.anivault.ui.dataclassess.HorizontalAnime

interface AnimeSearchRepository {
    suspend fun getTopAiringAnime(): List<HorizontalAnime>
    suspend fun getTopAnimeHorizontal(): List<HorizontalAnime>
    suspend fun getRecommendationAnimeHorizontal(): List<HorizontalAnime>
    suspend fun getAnimeListNextSeasonSearch(): List<HorizontalAnime>
}