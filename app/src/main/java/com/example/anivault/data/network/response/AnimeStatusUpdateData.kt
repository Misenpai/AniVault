package com.example.anivault.data.network.response

data class AnimeStatusUpdateData(
    val status: String,
    val mal_id: Int,
    val user_id: Int,
    val total_watched_episodes: Int
)

data class MessageResponseUpdate(val message: String)
data class AnimeStatusListResponseUpdate(val animes: List<AnimeStatusUpdateData>)