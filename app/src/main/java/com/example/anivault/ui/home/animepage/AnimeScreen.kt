package com.example.anivault.ui.home.animepage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.anivault.R
import com.example.anivault.data.network.response.AnimeDetails
import com.example.anivault.ui.home.AddingAnimeDatabase
import com.example.anivault.ui.viewmodel.AnimeDetailsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AnimeScreen : AppCompatActivity() {

    private val viewModel: AnimeDetailsViewModel by viewModels()
    private lateinit var webView: WebView
    private var videoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_screen)


        val animeId = intent.getIntExtra("animeId", 0)
        viewModel.fetchAnimeDetails(animeId)

        observeAnimeDetails()

        // Set up the back button
        findViewById<ImageView>(R.id.imageView3).setOnClickListener {
            onBackPressed()
        }

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            openAddingAnimeDatabase()
        }
    }


    private fun openAddingAnimeDatabase() {
        viewModel.animeDetails.value?.let { animeDetails ->
            val intent = Intent(this, AddingAnimeDatabase::class.java).apply {
                putExtra("mal_id", animeDetails.mal_id)
                putExtra("anime_title", animeDetails.title)
                putExtra("status", animeDetails.status)
                putExtra("episodes", animeDetails.episodes)
            }
            startActivity(intent)
        }
    }

    private fun observeAnimeDetails() {
        viewModel.animeDetails.observe(this, Observer { details ->
            details?.let {
                updateUI(it)
            } ?: run {
                showError()
            }
        })
    }

    private fun showError() {
        Toast.makeText(this, "Failed to fetch anime details", Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(details: AnimeDetails) {
        findViewById<TextView>(R.id.anime_title).text = details.title
        findViewById<TextView>(R.id.anime_title_japanese).text = details.title_japanese
        findViewById<TextView>(R.id.scoreText).text = details.score.toString()
        findViewById<TextView>(R.id.rankedText).text = details.rank.toString()
        findViewById<TextView>(R.id.episodesText).text = details.episodes.toString()
        findViewById<TextView>(R.id.statusText).text = details.status
        findViewById<TextView>(R.id.typeText).text = details.type
        findViewById<TextView>(R.id.sourceText).text = details.source
        findViewById<TextView>(R.id.airedText).text = details.aired.string
        findViewById<TextView>(R.id.ratingText).text = details.rating
        findViewById<TextView>(R.id.durationText).text = details.duration
        findViewById<TextView>(R.id.genresText).text = details.genres.joinToString { it.name }
        findViewById<TextView>(R.id.summary_text).text = details.synopsis
        findViewById<TextView>(R.id.anime_title_englishText).text = details.title_english
        findViewById<TextView>(R.id.seasonText).text = details.season
        findViewById<TextView>(R.id.yearText).text = details.year.toString()
        findViewById<TextView>(R.id.producerText).text = details.producers.joinToString { it.name }
        findViewById<TextView>(R.id.lisensorText).text = details.licensors.joinToString { it.name }
        findViewById<TextView>(R.id.studioText).text = details.studios.joinToString { it.name }
        findViewById<TextView>(R.id.demographText).text = details.demographics.joinToString { it.name }

        Glide.with(this)
            .load(details.images.jpg.large_image_url)
            .into(findViewById<ImageView>(R.id.mainCharacterImage))

        details.relations.forEach { relation ->
            when (relation.relation) {
                "Adaptation" -> findViewById<TextView>(R.id.relation_adaptation_name).text = relation.entry.firstOrNull()?.name ?: ""
                "Prequel" -> findViewById<TextView>(R.id.relation_prequel_name).text = relation.entry.firstOrNull()?.name ?: ""
                "Other" -> findViewById<TextView>(R.id.relation_other_name).text = relation.entry.firstOrNull()?.name ?: ""
            }
        }

        val openingLayout = findViewById<LinearLayout>(R.id.opening)
        openingLayout.removeAllViews()
        details.theme.openings.forEach { opening ->
            val textView = TextView(this)
            textView.text = opening
            textView.setTextColor(Color.WHITE)
            openingLayout.addView(textView)
        }

        val endingLayout = findViewById<LinearLayout>(R.id.ending)
        endingLayout.removeAllViews()
        details.theme.endings.forEach { ending ->
            val textView = TextView(this)
            textView.text = ending
            textView.setTextColor(Color.WHITE)
            endingLayout.addView(textView)
        }

    }

}