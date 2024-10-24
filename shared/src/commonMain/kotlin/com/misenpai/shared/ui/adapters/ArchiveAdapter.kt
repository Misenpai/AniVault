package com.misenpai.shared.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.misenpai.anivault.R

import com.misenpai.anivault.ui.dataclassess.ArchiveYearItems

class ArchiveAdapter(
    private val onSeasonClick: (Int, String) -> Unit
) : ListAdapter<ArchiveYearItems, ArchiveAdapter.ViewHolder>(ArchiveDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.archive_year_items, parent, false)
        return ViewHolder(view, onSeasonClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(view: View, private val onSeasonClick: (Int, String) -> Unit) : RecyclerView.ViewHolder(view) {
        private val yearTextView: TextView = view.findViewById(R.id.archive_year_id)
        private val seasonButtons: List<Button> = listOf(
            view.findViewById(R.id.archive_winter),
            view.findViewById(R.id.archive_spring),
            view.findViewById(R.id.archive_summer),
            view.findViewById(R.id.archive_fall)
        )

        fun bind(item: ArchiveYearItems) {
            yearTextView.text = item.year.toString()
            seasonButtons.forEachIndexed { index, button ->
                val season = SEASONS[index]
                if (season in item.availableSeasons) {
                    button.visibility = View.VISIBLE
                    button.text = season.capitalize()
                    button.setOnClickListener { onSeasonClick(item.year, season) }
                } else {
                    button.visibility = View.GONE
                }
            }
        }
    }

    class ArchiveDiffCallback : DiffUtil.ItemCallback<ArchiveYearItems>() {
        override fun areItemsTheSame(oldItem: ArchiveYearItems, newItem: ArchiveYearItems): Boolean {
            return oldItem.year == newItem.year
        }

        override fun areContentsTheSame(oldItem: ArchiveYearItems, newItem: ArchiveYearItems): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private val SEASONS = listOf("winter", "spring", "summer", "fall")
    }
}