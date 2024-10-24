package com.misenpai.shared.ui.home.animelibrarylayouts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.misenpai.anivault.R
import com.misenpai.shared.ui.adapters.AnimeStatusAdapterCompleted
import com.misenpai.anivault.ui.home.animepage.AnimeScreen
import com.misenpai.anivault.ui.viewmodel.CompletedViewModel
import com.misenpai.anivault.ui.viewmodel.ResultCompleted
import com.misenpai.anivault.ui.viewmodelfactory.LibraryViewModelFactory
import com.misenpai.anivault.utils.RevolvingProgressBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class Completed : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val viewModelFactory: LibraryViewModelFactory by instance()

    private lateinit var viewModel: CompletedViewModel
    private lateinit var adapter: _root_ide_package_.com.misenpai.shared.ui.adapters.AnimeStatusAdapterCompleted
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalAnimeText: TextView
    private lateinit var loadingProgressBar: RevolvingProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var currentUserId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_completed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CompletedViewModel::class.java)

        recyclerView = view.findViewById(R.id.recycleViewCompletedLibrary)
        totalAnimeText = view.findViewById(R.id.completed_total_anime_text)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBarCompleted)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutCompleted)

        viewModel.currentUserId.observe(viewLifecycleOwner) { userId ->
            currentUserId = userId
        }

        adapter = _root_ide_package_.com.misenpai.shared.ui.adapters.AnimeStatusAdapterCompleted(
            onItemClick = { anime ->
                val intent = Intent(requireContext(), AnimeScreen::class.java)
                intent.putExtra("animeId", anime.statusData.mal_id)
                startActivity(intent)
            },
            onModifyClick = { anime ->
                viewModel.currentUserId.value?.let { userId ->
                    viewModel.updateWatchedEpisodes(anime, userId)
                } ?: run {
                    Toast.makeText(context, "User ID not available", Toast.LENGTH_SHORT).show()
                }
            },
            onDeleteClick = { anime ->
                viewModel.currentUserId.value?.let { userId ->
                    viewModel.deleteAnimeStatus(anime.statusData.mal_id, userId)
                } ?: run {
                    Toast.makeText(context, "User ID not available", Toast.LENGTH_SHORT).show()
                }
            }
        )
        setupRecyclerView()
        observeViewModel()

        // Load the completed anime list initially
        viewModel.loadPlanToWatchAnime()

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Show loading indicator
                loadingProgressBar.visibility = View.VISIBLE
            } else {
                // Hide loading indicator
                loadingProgressBar.visibility = View.GONE
            }
        }

        viewModel.updateResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultCompleted.Success -> {
                    Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                }
                is ResultCompleted.Error -> {
                    Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.deleteResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultCompleted.Success -> {
                    Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                }
                is ResultCompleted.Error -> {
                    Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadPlanToWatchAnime()
            swipeRefreshLayout.isRefreshing = false  // Immediately hide the refresh animation
        }
    }

    private fun setupRecyclerView() {
        adapter = _root_ide_package_.com.misenpai.shared.ui.adapters.AnimeStatusAdapterCompleted(
            onItemClick = { anime ->
                val intent = Intent(requireContext(), AnimeScreen::class.java)
                intent.putExtra("animeId", anime.statusData.mal_id)
                startActivity(intent)
            },
            onModifyClick = { anime ->
                viewModel.currentUserId.value?.let { userId ->
                    viewModel.updateWatchedEpisodes(anime, userId)
                } ?: run {
                    Toast.makeText(context, "User ID not available", Toast.LENGTH_SHORT).show()
                }
            },
            onDeleteClick = { anime ->
                viewModel.currentUserId.value?.let { userId ->
                    viewModel.deleteAnimeStatus(anime.statusData.mal_id, userId)
                } ?: run {
                    Toast.makeText(context, "User ID not available", Toast.LENGTH_SHORT).show()
                }
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeViewModel() {
        viewModel.animeList.observe(viewLifecycleOwner) { animeList ->
            adapter.submitList(animeList)
            totalAnimeText.text = animeList.size.toString()
        }
    }
}
