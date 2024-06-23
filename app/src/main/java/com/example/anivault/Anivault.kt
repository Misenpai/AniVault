package com.example.anivault

import android.app.Application
import com.example.anivault.data.db.AppDatabase
import com.example.anivault.data.network.MyApi
import com.example.anivault.data.network.NetworkConnectionInterceptor
import com.example.anivault.data.repository.UserRepository
import com.example.anivault.ui.auth.AuthViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class Anivault : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@Anivault))
        bind<NetworkConnectionInterceptor>() with singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { UserRepository(instance(), instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
    }

}