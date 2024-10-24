
import com.misenpai.anivault.data.network.JikanApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.jikan.moe/v4/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: JikanApiService by lazy {
        retrofit.create(JikanApiService::class.java)
    }
}
