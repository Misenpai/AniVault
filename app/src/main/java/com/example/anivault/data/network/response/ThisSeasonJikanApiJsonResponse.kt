package com.example.anivault.data.network.response

data class AnimeResponse(
    val pagination: Pagination,
    val data: List<AnimeData>
)

data class Pagination(
    val last_visible_page: Int,
    val has_next_page: Boolean,
    val current_page: Int,
    val items: Items
)

data class Items(
    val count: Int,
    val total: Int,
    val per_page: Int
)

data class AnimeData(
    val mal_id: Int,
    val url: String,
    val images: Images,
    val title: String,
    val genres: List<Genre>
)

data class Images(
    val jpg: ImageDetails
)

data class ImageDetails(
    val image_url: String
)

data class Genre(
    val name: String
)
