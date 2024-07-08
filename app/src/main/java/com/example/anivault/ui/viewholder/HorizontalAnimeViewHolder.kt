package com.example.anivault.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R

class HorizontalAnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val animeTitle: TextView = itemView.findViewById(R.id.textView8)
    val animePic: ImageView = itemView.findViewById(R.id.imageView5)
}