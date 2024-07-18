package com.example.anivault.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R
import com.example.anivault.data.network.response.AnimeStatusData

class AnimeStatusAdapter : ListAdapter<AnimeStatusData, AnimeStatusAdapter.AnimeViewHolder>(AnimeStatusDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.library_card_view, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val animeName: TextView = itemView.findViewById(R.id.search_anime_name_library)
        private val watchedEpisodes: TextView = itemView.findViewById(R.id.currently_watched_episodes)
        private val totalEpisodes: TextView = itemView.findViewById(R.id.total_number_of_episodes)
        // Find other views as needed

        fun bind(anime: AnimeStatusData) {
            animeName.text = anime.anime_name
            watchedEpisodes.text = anime.total_watched_episodes.toString()
            totalEpisodes.text = anime.total_episodes.toString()
            // Set other views as needed
        }
    }
}

class AnimeStatusDiffCallback : DiffUtil.ItemCallback<AnimeStatusData>() {
    override fun areItemsTheSame(oldItem: AnimeStatusData, newItem: AnimeStatusData): Boolean {
        return oldItem.mal_id == newItem.mal_id
    }

    override fun areContentsTheSame(oldItem: AnimeStatusData, newItem: AnimeStatusData): Boolean {
        return oldItem == newItem
    }
}
