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
import com.example.anivault.data.db.AppDatabase
import com.example.anivault.data.network.MyApi
import com.example.anivault.data.network.NetworkConnectionInterceptor
import com.example.anivault.ui.adapters.AnimeStatusAdapter
import com.example.anivault.ui.viewmodel.DroppedViewModel
import com.example.anivault.ui.viewmodelfactory.LibraryViewModelFactory

class Dropped : Fragment() {

    private lateinit var viewModel: DroppedViewModel
    private lateinit var adapter: AnimeStatusAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalAnimeText: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dropped, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val api = MyApi.invoke(NetworkConnectionInterceptor(requireContext()))
        val db = AppDatabase(requireContext())
        viewModel = ViewModelProvider(this, LibraryViewModelFactory(api, db))
            .get(DroppedViewModel::class.java)

        recyclerView = view.findViewById(R.id.recycleViewDroppedLibrary)
        totalAnimeText = view.findViewById(R.id.dropped_total_anime_text)

        setupRecyclerView()
        observeViewModel()

        viewModel.loadPlanToWatchAnime()
    }

    private fun setupRecyclerView() {
        adapter = AnimeStatusAdapter()
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