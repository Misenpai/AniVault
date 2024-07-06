package com.example.anivault.data.network.response

data class SeasonResponse(
    val pagination: PaginationArchive,
    val data: List<YearData>
)

data class PaginationArchive(
    val last_visible_page: Int,
    val has_next_page: Boolean
)

data class YearData(
    val year: Int,
    val seasons: List<String>
)

