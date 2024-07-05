package com.example.anivault.ui.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R
import com.google.android.material.imageview.ShapeableImageView

class AnimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val animePic: ShapeableImageView = view.findViewById(R.id.anime_pic)
    val animeTitle: TextView = view.findViewById(R.id.anime_title)
    val animeTheme: TextView = view.findViewById(R.id.anime_theme)
}
