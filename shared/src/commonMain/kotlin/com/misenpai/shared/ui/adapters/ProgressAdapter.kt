    package com.misenpai.shared.ui.adapters

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.misenpai.anivault.R

    class ProgressAdapter(private val episodeCount: Int) : RecyclerView.Adapter<ProgressAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val numberTextView: TextView = view.findViewById(R.id.numberTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val actualPosition = position
            if (actualPosition in 0 until episodeCount+1) {
                holder.numberTextView.text = (actualPosition+1).toString()
                holder.itemView.visibility = View.VISIBLE
            } else {
                holder.itemView.visibility = View.INVISIBLE
            }
        }

        override fun getItemCount() = episodeCount
    }