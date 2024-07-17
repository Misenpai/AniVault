package com.example.anivault.data.network

import AuthResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MyApi {

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AuthResponse>


    @FormUrlEncoded
    @POST("/user/signup")
    suspend fun usersignup(
        @Field("name") name: String,
        @Field("email") email:String,
        @Field("password") password:String
    ):Response<AuthResponse>

    companion object{
            operator fun invoke(
                networkConnectionInterceptor:NetworkConnectionInterceptor
            ) : MyApi{

                val okkHttpclient = OkHttpClient.Builder()
                    .addInterceptor(networkConnectionInterceptor)
                    .build()

                return Retrofit.Builder()
                    .client(okkHttpclient)
                    .baseUrl("https://authorization-q1gn.onrender.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MyApi::class.java)
            }
    }
}