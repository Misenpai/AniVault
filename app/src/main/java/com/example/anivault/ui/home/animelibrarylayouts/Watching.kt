package com.example.anivault.ui.home.animelibrarylayouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R
import com.example.anivault.ui.adapters.AnimeStatusAdapterWatching
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

        setupRecyclerView()
        observeViewModel()

        viewModel.loadPlanToWatchAnime()
    }

    private fun setupRecyclerView() {
        adapter = AnimeStatusAdapterWatching()
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