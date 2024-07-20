package com.example.anivault.ui.home.animelibrarylayouts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R
import com.example.anivault.ui.adapters.AnimeStatusAdapterWatching
import com.example.anivault.ui.home.animepage.AnimeScreen
import com.example.anivault.ui.viewmodel.ResultWatching
import com.example.anivault.ui.viewmodel.WatchingViewModel
import com.example.anivault.ui.viewmodelfactory.LibraryViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class Watching : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val viewModelFactory: LibraryViewModelFactory by instance()

    private lateinit var viewModel: WatchingViewModel
    private lateinit var adapter: AnimeStatusAdapterWatching
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalAnimeText: TextView
    private lateinit var loadingProgressBar: ProgressBar

    private var currentUserId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_watching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(WatchingViewModel::class.java)

        recyclerView = view.findViewById(R.id.recycleViewWatchingLibrary)
        totalAnimeText = view.findViewById(R.id.watching_total_anime_text)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBarWatching)

        viewModel.currentUserId.observe(viewLifecycleOwner) { userId ->
            currentUserId = userId
        }

        adapter = AnimeStatusAdapterWatching(
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
            onDeleteClick = { anime ->  // Add this block
                viewModel.currentUserId.value?.let { userId ->
                    viewModel.deleteAnimeStatus(anime.statusData.mal_id, userId)
                } ?: run {
                    Toast.makeText(context, "User ID not available", Toast.LENGTH_SHORT).show()
                }
            }
        )

        setupRecyclerView()
        observeViewModel()

        viewModel.loadWatchingAnime()

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
                is ResultWatching.Success -> {
                    Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                }
                is ResultWatching.Error -> {
                    Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.deleteResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWatching.Success -> {
                    Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                }
                is ResultWatching.Error -> {
                    Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
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