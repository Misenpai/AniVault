package com.misenpai.shared.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.misenpai.anivault.ui.home.seasonlayouts.Archive
import com.misenpai.anivault.ui.home.seasonlayouts.Last
import com.misenpai.anivault.ui.home.seasonlayouts.Next
import com.misenpai.anivault.ui.home.seasonlayouts.ThisSeason

class FragmentSeasonLayoutAdapter(
    fragmentManager : FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Last()
            1 -> ThisSeason()
            2 -> Next()
            else -> Archive()
        }
    }
}