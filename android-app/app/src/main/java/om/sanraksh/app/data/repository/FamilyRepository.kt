package om.sanraksh.app.data.repository

import om.sanraksh.app.data.model.ElderEventsResponse
import om.sanraksh.app.data.model.ElderStatusResponse
import om.sanraksh.app.data.model.FamilyEldersResponse
import om.sanraksh.app.data.model.LinkElderRequest
import om.sanraksh.app.data.model.LinkElderResponse
import om.sanraksh.app.data.remote.RetrofitClient

class FamilyRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getLinkedElders(
        accessToken: String
    ): Result<FamilyEldersResponse> {
        return try {
            val response = apiService.getLinkedElders(
                authorization = "Bearer $accessToken"
            )
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Failed to load elders: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getElderStatus(
        accessToken: String,
        elderId: String
    ): Result<ElderStatusResponse> {
        return try {
            val response = apiService.getElderStatus(
                authorization = "Bearer $accessToken",
                elderId = elderId
            )
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Failed to load status: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getElderEvents(
        accessToken: String,
        elderId: String
    ): Result<ElderEventsResponse> {
        return try {
            val response = apiService.getElderEvents(
                authorization = "Bearer $accessToken",
                elderId = elderId
            )
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Failed to load events: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun linkElder(
        accessToken: String,
        elderId: String
    ): Result<LinkElderResponse> {
        return try {
            val response = apiService.linkElder(
                authorization = "Bearer $accessToken",
                request = LinkElderRequest(elderId = elderId)
            )
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = when (response.code()) {
                    400 -> "Invalid Elder ID"
                    404 -> "Elder not found"
                    409 -> "Elder is already linked"
                    403 -> "You don't have permission to link this elder"
                    else -> "Failed to link elder: ${response.code()}"
                }
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
