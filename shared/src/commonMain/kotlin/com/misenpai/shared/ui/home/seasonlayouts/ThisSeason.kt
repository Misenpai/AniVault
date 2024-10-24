package com.misenpai.shared.ui.home.seasonlayouts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.misenpai.anivault.R
import com.misenpai.anivault.ui.adapters.AnimeAdapter
import com.misenpai.anivault.ui.home.animepage.AnimeScreen
import com.misenpai.anivault.ui.viewmodel.AnimeViewModel
import com.misenpai.anivault.utils.RevolvingProgressBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ThisSeason : Fragment(), KodeinAware {
    override val kodein by kodein()

    private lateinit var animeAdapter: AnimeAdapter
    private lateinit var viewModel: AnimeViewModel
    private val viewModelFactory: AnimeViewModelFactory by instance()
    private lateinit var progressBar: RevolvingProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_this_season, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycleViewThisSeason)
        progressBar = view.findViewById(R.id.progressBarThisSeason)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutThisSeason)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        animeAdapter = AnimeAdapter { anime ->
            val intent = Intent(requireContext(), AnimeScreen::class.java)
            intent.putExtra("animeId", anime.mal_id)
            startActivity(intent)
        }
        recyclerView.adapter = animeAdapter

        viewModel = ViewModelProvider(this, viewModelFactory).get(AnimeViewModel::class.java)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.animeListThisSeason.observe(viewLifecycleOwner) { animeList ->
            animeAdapter.submitList(animeList)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchCurrentSeasonAnime()
            swipeRefreshLayout.isRefreshing = false  // Immediately hide the refresh animation
        }
    }
}