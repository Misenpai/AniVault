package com.misenpai.shared.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.misenpai.anivault.R
import com.misenpai.anivault.data.network.response.AnimeSearchItem

class SearchResultAdapter(private val onItemClick: (AnimeSearchItem) -> Unit) : ListAdapter<AnimeSearchItem, SearchResultAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.anime_search_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val mainCharacterImage: ImageView = view.findViewById(R.id.mainCharacterImage)
        private val animeName: TextView = view.findViewById(R.id.search_anime_name)
        private val animeType: TextView = view.findViewById(R.id.search_type)
        private val animeSeason: TextView = view.findViewById(R.id.search_season)
        private val animeYear: TextView = view.findViewById(R.id.search_year)
        private val genresText: TextView = view.findViewById(R.id.genres)

        fun bind(item: AnimeSearchItem) {
            animeName.text = item.title
            animeType.text = item.type
            animeSeason.text = item.season
            animeYear.text = item.year.toString()

            Glide.with(mainCharacterImage.context)
                .load(item.images.jpg.image_url)
                .placeholder(R.drawable.anime_poster)
                .error(R.drawable.anime_poster)
                .into(mainCharacterImage)

            val genresString = item.genres.joinToString(separator = " | ") { it.name }
            genresText.text = genresString
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AnimeSearchItem>() {
        override fun areItemsTheSame(oldItem: AnimeSearchItem, newItem: AnimeSearchItem): Boolean {
            return oldItem.mal_id == newItem.mal_id
        }

        override fun areContentsTheSame(oldItem: AnimeSearchItem, newItem: AnimeSearchItem): Boolean {
            return oldItem == newItem
        }
    }
}