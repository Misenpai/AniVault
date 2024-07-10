package com.example.anivault.ui.home.mainactivitylayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R
import com.example.anivault.ui.adapters.HorizontalAnimeAdapter
import com.example.anivault.ui.adapters.HorizontalAnimeRecommendedAdapter
import com.example.anivault.ui.home.seasonlayouts.AnimeViewModelFactory
import com.example.anivault.ui.viewmodel.SearchAnimeHorizontalViewHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AnimeSearch : Fragment(),KodeinAware {
    override val kodein by kodein()

    private lateinit var topAnimeAdapter: HorizontalAnimeAdapter
    private lateinit var recommendedAnimeAdapter: HorizontalAnimeRecommendedAdapter
    private lateinit var topUpcomingAdapter: HorizontalAnimeAdapter
    private lateinit var topAiringAdapter: HorizontalAnimeAdapter
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
        val recyclerViewTopAnime: RecyclerView = view.findViewById(R.id.recycleViewTopAnime)
        recyclerViewTopAnime.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        topAnimeAdapter = HorizontalAnimeAdapter()
        recyclerViewTopAnime.adapter = topAnimeAdapter

        val recyclerViewRecommendedAnime: RecyclerView = view.findViewById(R.id.recycleviewRecommendation)
        recyclerViewRecommendedAnime.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        recommendedAnimeAdapter = HorizontalAnimeRecommendedAdapter()
        recyclerViewRecommendedAnime.adapter = recommendedAnimeAdapter

        val recyclerViewTopUpcoming: RecyclerView = view.findViewById(R.id.recycleviewTopUpcoming)
        recyclerViewTopUpcoming.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        topUpcomingAdapter = HorizontalAnimeAdapter()
        recyclerViewTopUpcoming.adapter = topUpcomingAdapter

        val recyclerViewTopAiringAnime: RecyclerView = view.findViewById(R.id.recycleviewTopAiring)
        recyclerViewTopAiringAnime.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        topAiringAdapter = HorizontalAnimeAdapter()
        recyclerViewTopAiringAnime.adapter = topAiringAdapter

        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchAnimeHorizontalViewHolder ::class.java)
        viewModel.topAnimeList.observe(viewLifecycleOwner) { animeList ->
            topAnimeAdapter.submitList(animeList)
        }
        viewModel.recommendedAnimeList.observe(viewLifecycleOwner) { animeList ->
            recommendedAnimeAdapter.submitList(animeList)
        }
        viewModel.topUpcomingAnimeList.observe(viewLifecycleOwner) { animeList ->
            topUpcomingAdapter.submitList(animeList)
        }
        viewModel.topAiringAnimeList.observe(viewLifecycleOwner) { animeList ->
            topAiringAdapter.submitList(animeList)
        }
        viewModel.fetchTopAnime()
        viewLifecycleOwner.lifecycleScope.launch {
            delay(500) // Wait for 250ms
            viewModel.fetchRecommendedAnime()
            delay(500) // Wait for another 250ms
            viewModel.fetchTopUpcomingAnime()
            delay(500) // Wait for another 250ms
            viewModel.fetchTopAiringAnime()
        }
    }
}