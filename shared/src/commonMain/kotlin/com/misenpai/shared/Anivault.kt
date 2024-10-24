package com.misenpai.shared

import android.app.Application
import android.content.Context
import android.util.Log
import com.misenpai.anivault.data.db.AppDatabase
import com.misenpai.anivault.data.network.MyApi
import com.misenpai.anivault.data.network.NetworkConnectionInterceptor
import com.misenpai.anivault.data.repository.UserRepository
import com.misenpai.anivault.ui.auth.AuthViewModel
import com.misenpai.anivault.ui.auth.AuthViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { instance<AppDatabase>().getUserDao() }
        bind() from singleton { UserRepository(instance(), instance(), instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from singleton { AuthViewModel(instance()) }
        import(appModule)
    }

    override fun onCreate() {
        super.onCreate()

        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            val viewModel: AuthViewModel by kodein.instance()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    viewModel.clearUserData()
                    Log.d("Anivault", "User data cleared on first run")
                } catch (e: Exception) {
                    Log.e("Anivault", "Error clearing user data", e)
                }
            }

            prefs.edit().putBoolean("isFirstRun", false).apply()
        }

        Log.d("Anivault", "Application initialized")
    }
}