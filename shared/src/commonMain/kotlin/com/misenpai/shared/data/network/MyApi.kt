package com.misenpai.shared.data.network

import AuthResponse
import com.misenpai.anivault.data.network.response.AnimeStatusData
import com.misenpai.anivault.data.network.response.AnimeStatusListResponse
import com.misenpai.anivault.data.network.response.AnimeStatusUpdateData
import com.misenpai.anivault.data.network.response.MessageResponse
import com.misenpai.anivault.data.network.response.MessageResponseUpdate
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MyApi {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AuthResponse>


    @FormUrlEncoded
    @POST("user/signup")
    suspend fun usersignup(
        @Field("name") name: String,
        @Field("email") email:String,
        @Field("password") password:String
    ):Response<AuthResponse>

    @POST("user/anime/status")
    suspend fun insertAnimeStatus(@Body data: AnimeStatusData): Response<MessageResponse>

    @PUT("user/anime/status")
    suspend fun updateAnimeStatus(@Body data: AnimeStatusUpdateData): Response<MessageResponseUpdate>

    @DELETE("user/anime/status/{userId}/{malId}")
    suspend fun removeAnimeStatus(
        @Path("userId") userId: Int,
        @Path("malId") malId: Int
    ): Response<MessageResponse>

    @GET("user/anime/status/{userId}/{status}")
    suspend fun readAnimeStatus(
        @Path("userId") userId: Int,
        @Path("status") status: String
    ): Response<AnimeStatusListResponse>


    companion object{
            operator fun invoke(
                networkConnectionInterceptor:NetworkConnectionInterceptor
            ) : MyApi{

                val okkHttpclient = OkHttpClient.Builder()
                    .addInterceptor(networkConnectionInterceptor)
                    .build()

                return Retrofit.Builder()
                    .client(okkHttpclient)
                    .baseUrl("https://w5ixj64och.execute-api.us-east-1.amazonaws.com/anivault/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MyApi::class.java)
            }
    }
}