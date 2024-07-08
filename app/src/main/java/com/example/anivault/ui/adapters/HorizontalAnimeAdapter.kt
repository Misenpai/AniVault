package com.example.anivault.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.anivault.R
import com.example.anivault.ui.dataclassess.HorizontalAnime
import com.example.anivault.ui.viewholder.HorizontalAnimeViewHolder

class HorizontalAnimeAdapter : ListAdapter<HorizontalAnime, HorizontalAnimeViewHolder>(HorizontalAnimeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalAnimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.top_anime_card_view_horizontal, parent, false)
        return HorizontalAnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorizontalAnimeViewHolder, position: Int) {
        val anime = getItem(position)
        holder.animeTitle.text = anime.title

        Glide.with(holder.itemView.context)
            .load(anime.imageUrl)
            .into(holder.animePic)
    }

    class HorizontalAnimeDiffCallback : DiffUtil.ItemCallback<HorizontalAnime>() {
        override fun areItemsTheSame(oldItem: HorizontalAnime, newItem: HorizontalAnime): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: HorizontalAnime, newItem: HorizontalAnime): Boolean {
            return oldItem == newItem
        }
    }
}