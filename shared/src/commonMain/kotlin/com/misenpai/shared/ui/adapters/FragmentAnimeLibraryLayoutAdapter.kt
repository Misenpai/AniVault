package com.misenpai.shared.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.misenpai.anivault.ui.home.animelibrarylayouts.Completed
import com.misenpai.anivault.ui.home.animelibrarylayouts.Dropped
import com.misenpai.anivault.ui.home.animelibrarylayouts.PlantoWatch
import com.misenpai.anivault.ui.home.animelibrarylayouts.Watching

class FragmentAnimeLibraryLayoutAdapter(
    fragmentManager : FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Watching()
            1 -> Completed()
            2 -> Dropped()
            else -> PlantoWatch()
        }
    }
}