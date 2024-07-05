package com.example.anivault.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.anivault.ui.home.seasonlayouts.archive
import com.example.anivault.ui.home.seasonlayouts.last
import com.example.anivault.ui.home.seasonlayouts.next
import com.example.anivault.ui.home.seasonlayouts.thisSeason

class FragmentSeasonLayoutAdapter(
    fragmentManager : FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> last()
            1 -> thisSeason()
            2 -> next()
            else -> archive()
        }
    }
}