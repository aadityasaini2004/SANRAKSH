package om.sanraksh.app.data.repository

import om.sanraksh.app.data.model.AuthResponse
import om.sanraksh.app.data.model.LoginRequest
import om.sanraksh.app.data.model.RegisterRequest
import om.sanraksh.app.data.remote.ApiService

class AuthRepository(
    private val apiService: ApiService
) {

    suspend fun login(
        request: LoginRequest
    ): Result<AuthResponse> {
        return try {

            val response = apiService.login(request)

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Login failed: ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        request: RegisterRequest
    ): Result<AuthResponse> {
        return try {

            val response = apiService.register(request)

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Registration failed: ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}