package com.example.anivault.ui.home.animelibrarylayouts

import android.content.Intent
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
import com.example.anivault.ui.adapters.AnimeStatusAdapter
import com.example.anivault.ui.home.animepage.AnimeScreen
import com.example.anivault.ui.viewmodel.PlanToWatchViewModel
import com.example.anivault.ui.viewmodelfactory.LibraryViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class PlantoWatch : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val viewModelFactory: LibraryViewModelFactory by instance()

    private lateinit var viewModel: PlanToWatchViewModel
    private lateinit var adapter: AnimeStatusAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalAnimeText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_planto_watch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(PlanToWatchViewModel::class.java)

        recyclerView = view.findViewById(R.id.recycleViewPlantoWatchLibrary)
        totalAnimeText = view.findViewById(R.id.plantowatch_total_anime_text)

        adapter = AnimeStatusAdapter { anime ->
            val intent = Intent(requireContext(), AnimeScreen::class.java)
            intent.putExtra("animeId", anime.statusData.mal_id)
            startActivity(intent)
        }

        setupRecyclerView()
        observeViewModel()

        viewModel.loadPlanToWatchAnime()
    }

    private fun setupRecyclerView() {
        adapter = AnimeStatusAdapter{anime ->
            val intent = Intent(requireContext(), AnimeScreen::class.java)
            intent.putExtra("animeId", anime.statusData.mal_id)
            startActivity(intent)}
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
