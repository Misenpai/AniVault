package com.example.anivault

import com.example.anivault.data.network.JikanApiService
import com.example.anivault.data.repository.AnimeRepository
import com.example.anivault.ui.home.seasonlayouts.AnimeViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = Kodein.Module("appModule") {
    bind<JikanApiService>() with singleton {
        Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JikanApiService::class.java)
    }
    bind<AnimeRepository>() with singleton { AnimeRepository(instance()) }
    bind<AnimeViewModelFactory>() with provider { AnimeViewModelFactory(instance()) }
}