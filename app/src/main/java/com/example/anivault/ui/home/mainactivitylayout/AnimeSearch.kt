package com.example.anivault.ui.home.mainactivitylayout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R
import com.example.anivault.ui.adapters.HorizontalAnimeAdapter
import com.example.anivault.ui.adapters.HorizontalAnimeRecommendedAdapter
import com.example.anivault.ui.home.animepage.AnimeScreen
import com.example.anivault.ui.home.searchpage.SearchViewPage
import com.example.anivault.ui.home.seasonlayouts.AnimeViewModelFactory
import com.example.anivault.ui.viewmodel.SearchAnimeHorizontalViewHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AnimeSearch : Fragment(), KodeinAware {
    override val kodein by kodein()

    private lateinit var topAnimeAdapter: HorizontalAnimeAdapter
    private lateinit var recommendedAnimeAdapter: HorizontalAnimeRecommendedAdapter
    private lateinit var topUpcomingAdapter: HorizontalAnimeAdapter
    private lateinit var topAiringAdapter: HorizontalAnimeAdapter
    private lateinit var viewModel: SearchAnimeHorizontalViewHolder
    private val viewModelFactory: AnimeViewModelFactory by instance()

    private lateinit var searchView: SearchView
    private lateinit var scrollView: ScrollView
    private lateinit var contentContainer: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_anime_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.searchView)
        scrollView = view.findViewById(R.id.scrollView)
        contentContainer = view.findViewById(R.id.contentContainer)

        setupRecyclerViews(view)
        setupViewModel()
        setupSearchView()
    }

    private fun setupRecyclerViews(view: View) {
        val recyclerViewTopAnime: RecyclerView = view.findViewById(R.id.recycleViewTopAnime)
        recyclerViewTopAnime.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        topAnimeAdapter = HorizontalAnimeAdapter { anime ->
            navigateToAnimeScreen(anime.mal_id)
        }
        recyclerViewTopAnime.adapter = topAnimeAdapter

        val recyclerViewRecommendedAnime: RecyclerView = view.findViewById(R.id.recycleviewRecommendation)
        recyclerViewRecommendedAnime.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recommendedAnimeAdapter = HorizontalAnimeRecommendedAdapter { anime ->
            navigateToAnimeScreen(anime.mal_id)
        }
        recyclerViewRecommendedAnime.adapter = recommendedAnimeAdapter

        val recyclerViewTopUpcoming: RecyclerView = view.findViewById(R.id.recycleviewTopUpcoming)
        recyclerViewTopUpcoming.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        topUpcomingAdapter = HorizontalAnimeAdapter { anime ->
            navigateToAnimeScreen(anime.mal_id)
        }
        recyclerViewTopUpcoming.adapter = topUpcomingAdapter

        val recyclerViewTopAiringAnime: RecyclerView = view.findViewById(R.id.recycleviewTopAiring)
        recyclerViewTopAiringAnime.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        topAiringAdapter = HorizontalAnimeAdapter { anime ->
            navigateToAnimeScreen(anime.mal_id)
        }
        recyclerViewTopAiringAnime.adapter = topAiringAdapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchAnimeHorizontalViewHolder::class.java)
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
        fetchAnimeData()
    }

    private fun fetchAnimeData() {
        viewModel.fetchTopAnime()
        viewLifecycleOwner.lifecycleScope.launch {
            delay(500)
            viewModel.fetchRecommendedAnime()
            delay(500)
            viewModel.fetchTopUpcomingAnime()
            delay(500)
            viewModel.fetchTopAiringAnime()
        }
    }

    private var isSearchViewPageVisible = false

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    showSearchViewPage(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty() && isSearchViewPageVisible) {
                    returnToAnimeSearch()
                }
                return true
            }
        })

        searchView.setOnSearchClickListener {
            showSearchViewPage(null)
        }

        searchView.setOnCloseListener {
            returnToAnimeSearch()
            false
        }
    }


    private fun showSearchViewPage(query: String?) {
        if (!isSearchViewPageVisible) {
            val searchViewPage = if (query != null) SearchViewPage.newInstance(query) else SearchViewPage()
            childFragmentManager.beginTransaction()
                .replace(R.id.contentContainer, searchViewPage)
                .addToBackStack(null)
                .commit()
            scrollView.visibility = View.GONE
            isSearchViewPageVisible = true
        }
    }

    private fun returnToAnimeSearch() {
        if (isSearchViewPageVisible) {
            childFragmentManager.popBackStack()
            scrollView.visibility = View.VISIBLE
            searchView.clearFocus()
            isSearchViewPageVisible = false
        }
    }

    private fun navigateToAnimeScreen(animeId: Int) {
        val intent = Intent(requireContext(), AnimeScreen::class.java)
        intent.putExtra("animeId", animeId)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        // Only reset if SearchViewPage is not visible
        if (!isSearchViewPageVisible) {
            scrollView.visibility = View.VISIBLE
            childFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun onBackPressed(): Boolean {
        return if (isSearchViewPageVisible) {
            returnToAnimeSearch()
            true
        } else {
            false
        }
    }
}