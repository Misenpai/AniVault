package com.example.anivault.ui.home.animelibrarylayouts

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
import com.example.anivault.R
import com.example.anivault.ui.adapters.AnimeStatusAdapterCompleted
import com.example.anivault.ui.home.animepage.AnimeScreen
import com.example.anivault.ui.viewmodel.CompletedViewModel
import com.example.anivault.ui.viewmodelfactory.LibraryViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class Completed : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val viewModelFactory: LibraryViewModelFactory by instance()

    private lateinit var viewModel: CompletedViewModel
    private lateinit var adapter: AnimeStatusAdapterCompleted
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalAnimeText: TextView

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

        viewModel.currentUserId.observe(viewLifecycleOwner) { userId ->
            currentUserId = userId
        }

        adapter = AnimeStatusAdapterCompleted (
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


        viewModel.loadPlanToWatchAnime()
    }

    private fun setupRecyclerView() {
        adapter = AnimeStatusAdapterCompleted(
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