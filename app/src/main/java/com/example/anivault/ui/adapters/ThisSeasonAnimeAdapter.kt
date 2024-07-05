package com.example.anivault.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anivault.R
import com.example.anivault.ui.dataclassess.Anime
import com.example.anivault.ui.viewholder.AnimeViewHolder

class AnimeAdapter(private val animeList: List<Anime>) : RecyclerView.Adapter<AnimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.anime_list, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.animeTitle.text = anime.title
        holder.animeTheme.text = anime.genres.joinToString(", ")
        // Load image using an image loading library like Glide or Picasso
        Glide.with(holder.itemView.context)
            .load(anime.imageUrl)
            .into(holder.animePic)
    }

    override fun getItemCount(): Int = animeList.size
}
