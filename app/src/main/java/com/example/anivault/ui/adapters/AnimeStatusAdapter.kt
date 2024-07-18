package com.example.anivault.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anivault.R
import com.example.anivault.ui.viewmodel.AnimeStatusDataWithDetailsPlanToWatch

class AnimeStatusAdapter(private val onItemClick: (AnimeStatusDataWithDetailsPlanToWatch) -> Unit) : ListAdapter<AnimeStatusDataWithDetailsPlanToWatch, AnimeStatusAdapter.AnimeViewHolder>(AnimeStatusDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.library_card_view, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(getItem(position))
        val anime = getItem(position)

        holder.itemView.setOnClickListener {
            onItemClick(anime)
        }
    }

    class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val animeName: TextView = itemView.findViewById(R.id.search_anime_name_library)
        private val watchedEpisodes: TextView = itemView.findViewById(R.id.currently_watched_episodes)
        private val totalEpisodes: TextView = itemView.findViewById(R.id.total_number_of_episodes)
        private val mainCharacterImage: ImageView = itemView.findViewById(R.id.mainCharacterImage)
        private val searchType: TextView = itemView.findViewById(R.id.search_type)
        private val searchSeason: TextView = itemView.findViewById(R.id.search_season)
        private val searchYear: TextView = itemView.findViewById(R.id.search_year)

        fun bind(item: AnimeStatusDataWithDetailsPlanToWatch) {
            val anime = item.statusData
            val details = item.details

            animeName.text = anime.anime_name
            watchedEpisodes.text = anime.total_watched_episodes.toString()
            totalEpisodes.text = anime.total_episodes.toString()

            Glide.with(itemView.context)
                .load(details.images.jpg.image_url)
                .into(mainCharacterImage)

            searchType.text = details.type
            searchSeason.text = details.season
            searchYear.text = details.year.toString()
        }
    }
}

class AnimeStatusDiffCallback : DiffUtil.ItemCallback<AnimeStatusDataWithDetailsPlanToWatch>() {
    override fun areItemsTheSame(oldItem: AnimeStatusDataWithDetailsPlanToWatch, newItem: AnimeStatusDataWithDetailsPlanToWatch): Boolean {
        return oldItem.statusData.mal_id == newItem.statusData.mal_id
    }

    override fun areContentsTheSame(oldItem: AnimeStatusDataWithDetailsPlanToWatch, newItem: AnimeStatusDataWithDetailsPlanToWatch): Boolean {
        return oldItem == newItem
    }
}
