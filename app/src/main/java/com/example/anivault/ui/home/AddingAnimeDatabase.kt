package com.example.anivault.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.anivault.R
import com.example.anivault.data.db.AppDatabase
import com.example.anivault.data.network.MyApi
import com.example.anivault.data.network.NetworkConnectionInterceptor
import com.example.anivault.data.network.response.AnimeStatusData
import com.example.anivault.data.repository.UserRepository
import com.example.anivault.ui.adapters.ProgressAdapter
import com.example.anivault.ui.viewmodel.AddingAnimeDatabaseViewModel
import com.example.anivault.ui.viewmodel.Result
import com.example.anivault.ui.viewmodelfactory.AddingAnimeDatabaseViewModelFactory
import com.google.android.material.button.MaterialButton

class AddingAnimeDatabase : AppCompatActivity() {
    private lateinit var viewModel: AddingAnimeDatabaseViewModel
    private lateinit var progressAdapter: ProgressAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var snapHelper: LinearSnapHelper

    private lateinit var btnWatching: MaterialButton
    private lateinit var btnCompleted: MaterialButton
    private lateinit var btnPlanToWatch: MaterialButton
    private lateinit var btnDropped: MaterialButton
    private lateinit var btnSave: MaterialButton

    private var selectedButton: MaterialButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_anime_database)

        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)

        val api = MyApi(networkConnectionInterceptor)

        val db = AppDatabase(this)

        val userDao = db.getUserDao()

        val userRepository = UserRepository(api, db, userDao)

        val factory = AddingAnimeDatabaseViewModelFactory(userRepository)
        viewModel = ViewModelProvider(this, factory).get(AddingAnimeDatabaseViewModel::class.java)


        val malId = intent.getIntExtra("mal_id", 0)
        val animeTitle = intent.getStringExtra("anime_title") ?: ""
        val status = intent.getStringExtra("status") ?: ""
        val episodes = intent.getIntExtra("episodes", 0)

        findViewById<TextView>(R.id.anime_name_database).text = animeTitle
        findViewById<TextView>(R.id.anime_status_database).text = status
        findViewById<TextView>(R.id.progress_database).text = "$episodes ep"

        setupButtons()
        setupRecyclerView(episodes)
        setupSaveButton()
        observeCurrentUser()
        observeSaveResult()
    }

    private fun setupButtons() {
        btnWatching = findViewById(R.id.btnWatching)
        btnCompleted = findViewById(R.id.btnCompleted)
        btnPlanToWatch = findViewById(R.id.btnPlanToWatch)
        btnDropped = findViewById(R.id.btnDropped)

        val buttons = listOf(btnWatching, btnCompleted, btnPlanToWatch, btnDropped)
        val animeStatus = findViewById<TextView>(R.id.anime_status_database).text.toString()

        buttons.forEach { button ->
            button.setOnClickListener {
                if (animeStatus == "Not yet aired" && button != btnPlanToWatch) {
                    return@setOnClickListener
                }

                selectedButton?.isSelected = false
                button.isSelected = true
                selectedButton = button
            }

            if (animeStatus == "Not yet aired" && button != btnPlanToWatch) {
                button.isEnabled = false
            }
        }
    }

    private fun setupRecyclerView(episodes: Int) {
        recyclerView = findViewById(R.id.progressRecyclerView)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        progressAdapter = ProgressAdapter(episodes)
        recyclerView.adapter = progressAdapter

        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = screenWidth / 3

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val position = parent.getChildAdapterPosition(view)
                if (position == 0 || position == progressAdapter.itemCount - 1) {
                    val padding = (screenWidth - itemWidth) / 2
                    if (position == 0) {
                        outRect.left = padding
                    } else {
                        outRect.right = padding
                    }
                }
            }
        })

        recyclerView.scrollToPosition(episodes / 2)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                updateSelectedNumber()
            }
        })
    }


    private fun updateSelectedNumber() {
        val centerView = snapHelper.findSnapView(recyclerView.layoutManager)
        val centerPosition = centerView?.let { recyclerView.layoutManager?.getPosition(it) } ?: -1
        if (centerPosition != -1) {
            val selectedNumber = centerPosition - 1
            findViewById<TextView>(R.id.progress_database).text = "${selectedNumber + 2} ep"
        }
    }

    private fun setupSaveButton() {
        btnSave = findViewById(R.id.btnSave)
        btnSave.setOnClickListener {
            val userId = viewModel.getCurrentUserId()
            if (userId == null) {
                Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val malId = intent.getIntExtra("mal_id", 0)
            val animeTitle = intent.getStringExtra("anime_title") ?: ""
            val totalEpisodes = intent.getIntExtra("episodes", 0)
            val status = getSelectedStatus()

            val watchedEpisodes = when {
                btnCompleted.isSelected -> totalEpisodes
                btnPlanToWatch.isSelected -> 0
                else -> getSelectedEpisodes() + 1
            }

            val animeStatusData = AnimeStatusData(
                user_id = userId,
                mal_id = malId,
                anime_name = animeTitle,
                total_watched_episodes = watchedEpisodes,
                total_episodes = totalEpisodes,
                status = status
            )

            viewModel.saveOrUpdateAnimeStatus(animeStatusData)
        }
    }

    private fun getSelectedEpisodes(): Int {
        val centerView = snapHelper.findSnapView(recyclerView.layoutManager)
        val centerPosition = centerView?.let { recyclerView.layoutManager?.getPosition(it) } ?: -1
        return if (centerPosition != -1) centerPosition else 0
    }

    private fun getSelectedStatus(): String {
        return when {
            btnWatching.isSelected -> "Currently Watching"
            btnCompleted.isSelected -> "Completed"
            btnPlanToWatch.isSelected -> "Plan to Watch"
            btnDropped.isSelected -> "Dropped"
            else -> "Plan to Watch"
        }
    }

    private fun observeCurrentUser() {
        viewModel.currentUser.observe(this) { user ->
            if (user == null) {
                Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun observeSaveResult() {
        viewModel.saveResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(this, "Anime status saved successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after successful save
                }
                is Result.Error -> {
                    Toast.makeText(this, "Error saving anime status: ${result.exception.message}", Toast.LENGTH_LONG).show()
                }

                else -> {
                    Toast.makeText(this, "Unknown result", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
