package com.example.anivault.data.network.response

data class AnimeDetailsResponse(
    val data: AnimeDetails
)

data class AnimeDetails(
    val mal_id: Int,
    val url: String,
    val images: AnimeImages,
    val trailer: AnimeTrailer,
    val title: String,
    val title_english: String,
    val title_japanese: String,
    val type: String,
    val source: String,
    val episodes: Int,
    val status: String,
    val airing: Boolean,
    val aired: AnimeAired,
    val duration: String,
    val rating: String,
    val score: Double,
    val rank: Int,
    val synopsis: String,
    val season: String,
    val year: Int,
    val broadcast: AnimeBroadcast,
    val producers: List<AnimeEntity>,
    val licensors: List<AnimeEntity>,
    val studios: List<AnimeEntity>,
    val genres: List<AnimeEntity>,
    val themes: List<AnimeEntity>,
    val demographics: List<AnimeEntity>,
    val relations: List<AnimeRelation>,
    val theme: AnimeTheme,
    val external: List<AnimeExternal>,
    val streaming: List<AnimeExternal>
)

data class AnimeImages(
    val jpg: ImageUrlsAnime,
    val webp: ImageUrlsAnime
)

data class ImageUrlsAnime(
    val image_url: String,
    val small_image_url: String,
    val large_image_url: String
)

data class AnimeTrailer(
    val youtube_id: String?,
    val url: String?,
    val embed_url: String?,
    val images: TrailerImages?
)

data class TrailerImages(
    val image_url: String?,
    val small_image_url: String?,
    val medium_image_url: String?,
    val large_image_url: String?,
    val maximum_image_url: String?
)

data class AnimeAired(
    val from: String,
    val to: String?,
    val prop: AiredProp,
    val string: String
)

data class AiredProp(
    val from: DateProp,
    val to: DateProp?
)

data class DateProp(
    val day: Int?,
    val month: Int?,
    val year: Int?
)

data class AnimeBroadcast(
    val day: String?,
    val time: String?,
    val timezone: String?,
    val string: String?
)

data class AnimeEntity(
    val mal_id: Int,
    val type: String,
    val name: String,
    val url: String
)

data class AnimeRelation(
    val relation: String,
    val entry: List<AnimeEntity>
)

data class AnimeTheme(
    val openings: List<String>,
    val endings: List<String>
)

data class AnimeExternal(
    val name: String,
    val url: String
)