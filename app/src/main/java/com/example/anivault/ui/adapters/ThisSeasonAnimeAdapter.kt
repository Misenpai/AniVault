package com.example.anivault.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.anivault.R
import com.example.anivault.ui.dataclassess.Anime
import com.example.anivault.ui.viewholder.AnimeViewHolder

class AnimeAdapter(private val onItemClick: (Anime) -> Unit) : ListAdapter<Anime, AnimeViewHolder>(AnimeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.anime_list, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = getItem(position)
        holder.animeTitle.text = anime.title
        holder.animeTheme.text = anime.genres.joinToString(", ")
        Glide.with(holder.itemView.context)
            .load(anime.imageUrl)
            .into(holder.animePic)

        holder.itemView.setOnClickListener {
            onItemClick(anime)
        }
    }

    class AnimeDiffCallback : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem.mal_id == newItem.mal_id
        }

        override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem == newItem
        }
    }
}