package om.sanraksh.app.data.repository


import om.sanraksh.app.data.model.SafetyResponse
import om.sanraksh.app.data.remote.RetrofitClient

class SafetyRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun checkIn(
        accessToken: String
    ): Result<SafetyResponse> {

        return try {
            val response = apiService.checkIn(
                authorization = "Bearer $accessToken"
            )

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Check-in failed: ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun triggerSOS(
        accessToken: String
    ): Result<SafetyResponse> {

        return try {
            val response = apiService.triggerSOS(
                authorization = "Bearer $accessToken"
            )

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("SOS failed: ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}