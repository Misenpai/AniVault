package com.misenpai.shared.ui.home.mainactivitylayout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misenpai.anivault.R
import com.misenpai.anivault.ui.adapters.HorizontalAnimeAdapter
import com.misenpai.anivault.ui.adapters.HorizontalAnimeRecommendedAdapter
import com.misenpai.anivault.ui.home.animepage.AnimeScreen
import com.misenpai.anivault.ui.home.searchpage.SearchViewPage
import com.misenpai.anivault.ui.home.seasonlayouts.AnimeViewModelFactory
import com.misenpai.anivault.ui.viewmodel.AnimeSearchUiState
import com.misenpai.anivault.ui.viewmodel.SearchAnimeHorizontalViewHolder
import com.misenpai.anivault.utils.RevolvingProgressBar
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
    private lateinit var loadingProgressBar: RevolvingProgressBar

    private lateinit var searchView: SearchView
    private lateinit var scrollView: ScrollView
    private lateinit var contentContainer: FrameLayout

    private var isSearchViewPageVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_anime_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupRecyclerViews(view)
        setupViewModel()
        observeUiState()
        setupSearchView()

        viewModel.fetchAllAnimeData()

        // Handle back press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isSearchViewPageVisible) {
                    returnToAnimeSearch()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    private fun initViews(view: View) {
        searchView = view.findViewById(R.id.searchView)
        scrollView = view.findViewById(R.id.scrollView)
        contentContainer = view.findViewById(R.id.contentContainer)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBarSearch)
    }

    private fun setupRecyclerViews(view: View) {
        topAnimeAdapter = HorizontalAnimeAdapter { anime -> navigateToAnimeScreen(anime.mal_id) }
        recommendedAnimeAdapter =
            HorizontalAnimeRecommendedAdapter { anime -> navigateToAnimeScreen(anime.mal_id) }
        topUpcomingAdapter = HorizontalAnimeAdapter { anime -> navigateToAnimeScreen(anime.mal_id) }
        topAiringAdapter = HorizontalAnimeAdapter { anime -> navigateToAnimeScreen(anime.mal_id) }

        view.findViewById<RecyclerView>(R.id.recycleViewTopAnime).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = topAnimeAdapter
        }

        view.findViewById<RecyclerView>(R.id.recycleviewRecommendation).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAnimeAdapter
        }

        view.findViewById<RecyclerView>(R.id.recycleviewTopUpcoming).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = topUpcomingAdapter
        }

        view.findViewById<RecyclerView>(R.id.recycleviewTopAiring).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = topAiringAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(SearchAnimeHorizontalViewHolder::class.java)
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AnimeSearchUiState.Loading -> showLoading()
                is AnimeSearchUiState.Success -> showContent(state)
                is AnimeSearchUiState.Error -> showError(state.message)
            }
        }
    }

    private fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
        scrollView.visibility = View.GONE
        hideTextViews()
    }

    private fun showContent(state: AnimeSearchUiState.Success) {
        loadingProgressBar.visibility = View.GONE
        scrollView.visibility = View.VISIBLE

        topAiringAdapter.submitList(state.topAiring)
        topAnimeAdapter.submitList(state.topAnime)
        recommendedAnimeAdapter.submitList(state.recommended)
        topUpcomingAdapter.submitList(state.topUpcoming)

        showTextViews()
    }

    private fun hideTextViews() {
        view?.findViewById<TextView>(R.id.TopAiring)?.visibility = View.GONE
        view?.findViewById<TextView>(R.id.TopAnime)?.visibility = View.GONE
        view?.findViewById<TextView>(R.id.Recommendation)?.visibility = View.GONE
        view?.findViewById<TextView>(R.id.TopUpcoming)?.visibility = View.GONE
    }

    private fun showTextViews() {
        view?.findViewById<TextView>(R.id.TopAiring)?.visibility = View.VISIBLE
        view?.findViewById<TextView>(R.id.TopAnime)?.visibility = View.VISIBLE
        view?.findViewById<TextView>(R.id.Recommendation)?.visibility = View.VISIBLE
        view?.findViewById<TextView>(R.id.TopUpcoming)?.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        loadingProgressBar.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

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
            val searchViewPage =
                if (query != null) SearchViewPage.newInstance(query) else SearchViewPage()
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