package com.example.anivault.ui.adapters

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
import com.example.anivault.R
import com.example.anivault.ui.viewmodel.AnimeStatusDataWithDetailsWatching

class AnimeStatusAdapterWatching(
    private val onItemClick: (AnimeStatusDataWithDetailsWatching) -> Unit,
    private val onModifyClick: (AnimeStatusDataWithDetailsWatching) -> Unit,
    private val onDeleteClick: (AnimeStatusDataWithDetailsWatching) -> Unit
) : ListAdapter<AnimeStatusDataWithDetailsWatching, AnimeStatusAdapterWatching.AnimeViewHolderWatching>(AnimeStatusDiffCallbackWatching()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolderWatching {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.library_card_view, parent, false)
        return AnimeViewHolderWatching(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolderWatching, position: Int) {
        val anime = getItem(position)
        holder.bind(anime)

        holder.itemView.setOnClickListener { onItemClick(anime) }
        holder.modifyButton.setOnClickListener { onModifyClick(anime) }
        holder.deleteButton.setOnClickListener { onDeleteClick(anime) }
    }

    class AnimeViewHolderWatching(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        fun bind(item: AnimeStatusDataWithDetailsWatching) {
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

class AnimeStatusDiffCallbackWatching : DiffUtil.ItemCallback<AnimeStatusDataWithDetailsWatching>() {
    override fun areItemsTheSame(oldItem: AnimeStatusDataWithDetailsWatching, newItem: AnimeStatusDataWithDetailsWatching): Boolean {
        return oldItem.statusData.mal_id == newItem.statusData.mal_id
    }

    override fun areContentsTheSame(oldItem: AnimeStatusDataWithDetailsWatching, newItem: AnimeStatusDataWithDetailsWatching): Boolean {
        return oldItem == newItem
    }
}