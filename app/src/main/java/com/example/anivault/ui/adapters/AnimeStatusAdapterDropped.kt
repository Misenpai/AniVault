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
import com.example.anivault.ui.viewmodel.AnimeStatusDataWithDetailsDropped

class AnimeStatusAdapterDropped(private val onItemClick: (AnimeStatusDataWithDetailsDropped,) -> Unit,
                                private val onModifyClick: (AnimeStatusDataWithDetailsDropped) -> Unit,
                                private val onDeleteClick: (AnimeStatusDataWithDetailsDropped) -> Unit)
    : ListAdapter<AnimeStatusDataWithDetailsDropped, AnimeStatusAdapterDropped.AnimeViewHolderDropped>(AnimeStatusDiffCallbackDropped()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolderDropped {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.library_card_view, parent, false)
        return AnimeViewHolderDropped(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolderDropped, position: Int) {
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

    class AnimeViewHolderDropped(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        fun bind(item: AnimeStatusDataWithDetailsDropped) {
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

class AnimeStatusDiffCallbackDropped : DiffUtil.ItemCallback<AnimeStatusDataWithDetailsDropped>() {
    override fun areItemsTheSame(oldItem: AnimeStatusDataWithDetailsDropped, newItem: AnimeStatusDataWithDetailsDropped): Boolean {
        return oldItem.statusData.mal_id == newItem.statusData.mal_id
    }

    override fun areContentsTheSame(oldItem: AnimeStatusDataWithDetailsDropped, newItem: AnimeStatusDataWithDetailsDropped): Boolean {
        return oldItem == newItem
    }
}