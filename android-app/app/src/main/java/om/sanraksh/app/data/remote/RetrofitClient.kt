package om.sanraksh.app.data.remote

import android.content.Context
import om.sanraksh.app.data.local.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:5000/"

    lateinit var tokenManager: TokenManager
        private set

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor(tokenManager)
            )
            .build()
    }

    lateinit var apiService: ApiService
        private set

    fun initialize(context: Context) {

        tokenManager = TokenManager(context.applicationContext)

        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }
}