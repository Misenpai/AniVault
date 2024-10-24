package com.misenpai.shared.data.network.response

data class AnimeSearchItem(
    val mal_id: Int,
    val title: String,
    val images: ImagesSearch,
    val type: String?,
    val season: String?,
    val year: Int?,
    val genres: List<Genre> = emptyList()
)

data class ImagesSearch(
    val jpg: ImageUrlsSearch,
    val webp: ImageUrlsSearch
)

data class ImageUrlsSearch(
    val image_url: String,
    val small_image_url: String,
    val large_image_url: String
)

data class GenreSearch(
    val mal_id: Int,
    val type: String,
    val name: String
)