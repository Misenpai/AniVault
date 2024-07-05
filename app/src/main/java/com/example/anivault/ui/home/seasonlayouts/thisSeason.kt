package com.example.anivault.ui.home.seasonlayouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R
import com.example.anivault.ui.adapters.AnimeAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class thisSeason : Fragment(), KodeinAware {
    override val kodein by kodein()

    private lateinit var animeAdapter: AnimeAdapter
    private lateinit var viewModel: AnimeViewModel
    private val viewModelFactory: AnimeViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_this_season, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycleViewThisSeason)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel = ViewModelProvider(this, viewModelFactory).get(AnimeViewModel::class.java)
        viewModel.animeList.observe(viewLifecycleOwner, Observer { animeList ->
            animeAdapter = AnimeAdapter(animeList)
            recyclerView.adapter = animeAdapter
        })
    }
}