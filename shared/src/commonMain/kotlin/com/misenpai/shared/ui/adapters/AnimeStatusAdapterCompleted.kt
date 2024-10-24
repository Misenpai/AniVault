package com.misenpai.shared.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.misenpai.anivault.R
import com.misenpai.anivault.ui.viewmodel.AnimeStatusDataWithDetailsCompleted

class AnimeStatusAdapterCompleted(private val onItemClick: (AnimeStatusDataWithDetailsCompleted) -> Unit,
                                  private val onModifyClick: (AnimeStatusDataWithDetailsCompleted) -> Unit,
                                  private val onDeleteClick: (AnimeStatusDataWithDetailsCompleted) -> Unit)
    : ListAdapter<AnimeStatusDataWithDetailsCompleted, com.misenpai.shared.ui.adapters.AnimeStatusAdapterCompleted.AnimeViewHolderCompleted>(
    _root_ide_package_.com.misenpai.shared.ui.adapters.AnimeStatusDiffCallbackCompleted()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): _root_ide_package_.com.misenpai.shared.ui.adapters.AnimeStatusAdapterCompleted.AnimeViewHolderCompleted {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.library_card_view, parent, false)
        return _root_ide_package_.com.misenpai.shared.ui.adapters.AnimeStatusAdapterCompleted.AnimeViewHolderCompleted(
            view
        )
    }

    override fun onBindViewHolder(holder: _root_ide_package_.com.misenpai.shared.ui.adapters.AnimeStatusAdapterCompleted.AnimeViewHolderCompleted, position: Int) {
        holder.bind(getItem(position))
        val anime = getItem(position)

        holder.itemView.setOnClickListener {
            onItemClick(anime)
        }

        holder.modifyButton.setOnClickListener {
            onModifyClick(anime)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(anime)
        }
    }

    class AnimeViewHolderCompleted(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val animeName: TextView = itemView.findViewById(R.id.search_anime_name_library)
        private val watchedEpisodes: TextView = itemView.findViewById(R.id.currently_watched_episodes)
        private val totalEpisodes: TextView = itemView.findViewById(R.id.total_number_of_episodes)
        private val mainCharacterImage: ImageView = itemView.findViewById(R.id.mainCharacterImage)
        private val searchType: TextView = itemView.findViewById(R.id.search_type)
        private val searchSeason: TextView = itemView.findViewById(R.id.search_season)
        private val searchYear: TextView = itemView.findViewById(R.id.search_year)
        val modifyButton: ImageView = itemView.findViewById(R.id.modify_anime_count)
        val deleteButton: ImageView = itemView.findViewById(R.id.edit_anime_record)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bind(item: AnimeStatusDataWithDetailsCompleted) {
            val anime = item.statusData
            val details = item.details

            animeName.text = anime.anime_name
            updateProgress(anime.total_watched_episodes, anime.total_episodes)

            Glide.with(itemView.context)
                .load(details.images.jpg.image_url)
                .into(mainCharacterImage)

            searchType.text = details.type
            searchSeason.text = details.season
            searchYear.text = details.year.toString()
        }
        private fun updateProgress(watched: Int, total: Int) {
            progressBar.max = total
            progressBar.progress = watched
            watchedEpisodes.text = watched.toString()
            totalEpisodes.text = total.toString()
        }
    }
}

class AnimeStatusDiffCallbackCompleted : DiffUtil.ItemCallback<AnimeStatusDataWithDetailsCompleted>() {
    override fun areItemsTheSame(oldItem: AnimeStatusDataWithDetailsCompleted, newItem: AnimeStatusDataWithDetailsCompleted): Boolean {
        return oldItem.statusData.mal_id == newItem.statusData.mal_id
    }

    override fun areContentsTheSame(oldItem: AnimeStatusDataWithDetailsCompleted, newItem: AnimeStatusDataWithDetailsCompleted): Boolean {
        return oldItem == newItem
    }
}