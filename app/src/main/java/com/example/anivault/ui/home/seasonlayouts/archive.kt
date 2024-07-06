package com.example.anivault.ui.home.seasonlayouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R
import com.example.anivault.ui.adapters.ArchiveAdapter
import com.example.anivault.ui.viewmodel.ArchiveViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class archive : Fragment(), KodeinAware {

    override val kodein by kodein()

    private lateinit var adapter: ArchiveAdapter
    private lateinit var viewModel: ArchiveViewModel
    private val viewModelFactory: AnimeViewModelFactory by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_archive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ArchiveViewModel::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycleViewArchive)
        adapter = ArchiveAdapter { year, season ->
            viewModel.onSeasonButtonClicked(year, season)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel.archiveItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }
    }
}