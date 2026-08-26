package om.sanraksh.app.data.remote

import om.sanraksh.app.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val accessToken = tokenManager.getAccessToken()

        val requestBuilder = originalRequest.newBuilder()

        if (!accessToken.isNullOrBlank()) {
            requestBuilder.addHeader(
                "Authorization",
                "Bearer $accessToken"
            )
        }

        return chain.proceed(
            requestBuilder.build()
        )
    }
}