package om.sanraksh.app.data.remote

import om.sanraksh.app.data.model.AuthResponse
import om.sanraksh.app.data.model.LoginRequest
import om.sanraksh.app.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>
}