package com.example.anivault.ui.home.mainactivitylayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R
import com.example.anivault.ui.adapters.HorizontalAnimeAdapter
import com.example.anivault.ui.home.seasonlayouts.AnimeViewModelFactory
import com.example.anivault.ui.viewmodel.SearchAnimeHorizontalViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AnimeSearch : Fragment(),KodeinAware {
    override val kodein by kodein()

    private lateinit var animeAdapter: HorizontalAnimeAdapter
    private lateinit var viewModel: SearchAnimeHorizontalViewHolder
    private val viewModelFactory: AnimeViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anime_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycleViewTopAnime)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        animeAdapter = HorizontalAnimeAdapter()
        recyclerView.adapter = animeAdapter

        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchAnimeHorizontalViewHolder ::class.java)
        viewModel.animeList.observe(viewLifecycleOwner) { animeList ->
            animeAdapter.submitList(animeList)
        }
        viewModel.fetchTopAnime()
    }

}