package com.misenpai.shared.ui.home.seasonlayouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.misenpai.anivault.data.repository.AnimeRepository
import com.misenpai.anivault.ui.viewmodel.AnimeArchiveThatSeason
import com.misenpai.anivault.ui.viewmodel.AnimeViewModel
import com.misenpai.anivault.ui.viewmodel.AnimeViewModelNextAnime
import com.misenpai.anivault.ui.viewmodel.AnimeViewModelPreviousSeason
import com.misenpai.anivault.ui.viewmodel.ArchiveViewModel
import com.misenpai.anivault.ui.viewmodel.SearchAnimeHorizontalViewHolder

class AnimeViewModelFactory(private val repository: AnimeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnimeViewModel(repository) as T
        }
        else if (modelClass.isAssignableFrom(AnimeViewModelNextAnime::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnimeViewModelNextAnime(repository) as T
        }

        else if (modelClass.isAssignableFrom(AnimeViewModelPreviousSeason::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnimeViewModelPreviousSeason(repository) as T
        }
        else if (modelClass.isAssignableFrom(ArchiveViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArchiveViewModel(repository) as T
        }
        else if (modelClass.isAssignableFrom(AnimeArchiveThatSeason::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnimeArchiveThatSeason(repository) as T
        }
        else if (modelClass.isAssignableFrom(SearchAnimeHorizontalViewHolder::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchAnimeHorizontalViewHolder(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
