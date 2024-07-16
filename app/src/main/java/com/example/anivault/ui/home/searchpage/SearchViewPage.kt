package com.example.anivault.ui.home.searchpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R
import com.example.anivault.data.network.JikanApiService
import com.example.anivault.ui.adapters.SearchResultAdapter
import com.example.anivault.ui.viewmodel.SearchViewModel
import com.example.anivault.ui.viewmodelfactory.SearchViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewPage : Fragment() {
    companion object {
        private const val ARG_QUERY = "query"

        fun newInstance(query: String): SearchViewPage {
            val fragment = SearchViewPage()
            val args = Bundle()
            args.putString(ARG_QUERY, query)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchResultAdapter
    private lateinit var viewModel: SearchViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jikanApiService = retrofit.create(JikanApiService::class.java)
        val viewModelFactory = SearchViewModelFactory(jikanApiService)

        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_view_page, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewSearchView)
        progressBar = view.findViewById(R.id.progressBarSearchView)
        errorText = view.findViewById(R.id.errorTextSearchView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchResultAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = searchAdapter

        arguments?.getString(ARG_QUERY)?.let { query ->
            viewModel.searchAnime(query)
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            searchAdapter.submitList(results)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorText.text = errorMessage
            errorText.visibility = if (errorMessage != null) View.VISIBLE else View.GONE
        }
    }
}