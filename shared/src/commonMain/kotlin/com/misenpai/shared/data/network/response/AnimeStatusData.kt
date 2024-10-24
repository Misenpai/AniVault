package com.misenpai.shared.data.network.response

data class AnimeStatusData(
    val user_id: Int,
    val mal_id: Int,
    val anime_name: String,
    val total_watched_episodes: Int,
    val total_episodes: Int,
    val status: String
)

data class MessageResponse(val message: String)
data class AnimeStatusListResponse(val animes: List<AnimeStatusData>)