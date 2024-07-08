package com.example.anivault.data.network.response

data class AnimeRecommendationResponse(
    val pagination: RecommendationPagination,
    val data: List<RecommendationEntry>
)

data class RecommendationPagination(
    val last_visible_page: Int,
    val has_next_page: Boolean
)

data class RecommendationEntry(
    val mal_id: String,
    val entry: List<AnimeEntry>,
    val content: String,
    val date: String,
    val user: User
)

data class AnimeEntry(
    val mal_id: Int,
    val url: String,
    val images: RecommendationImages,
    val title: String
)

data class RecommendationImages(
    val jpg: ImageUrls,
    val webp: ImageUrls
)

data class ImageUrls(
    val image_url: String,
    val small_image_url: String,
    val large_image_url: String
)

data class User(
    val url: String,
    val username: String
)