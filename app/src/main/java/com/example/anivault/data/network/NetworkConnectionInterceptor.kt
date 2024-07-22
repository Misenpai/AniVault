package com.example.anivault.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.example.anivault.utils.NoInternetException
import com.example.anivault.utils.TimeoutException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException

class NetworkConnectionInterceptor(
    context: Context
) : Interceptor {

    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isInternetAvailable())
            throw NoInternetException("Make sure you have an active data connection")
        return try {
            chain.proceed(chain.request())
        } catch (e: SocketTimeoutException) {
            throw TimeoutException("Connection timeout. Please try again.")
        } catch (e: IOException) {
            throw NoInternetException("Network error. Please check your connection.")
        }
    }

    private fun isInternetAvailable() : Boolean{
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }
}