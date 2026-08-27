package om.sanraksh.app.data.remote

import om.sanraksh.app.data.model.AuthResponse
import om.sanraksh.app.data.model.ElderEventsResponse
import om.sanraksh.app.data.model.ElderStatusResponse
import om.sanraksh.app.data.model.FamilyEldersResponse
import om.sanraksh.app.data.model.LinkElderRequest
import om.sanraksh.app.data.model.LinkElderResponse
import om.sanraksh.app.data.model.LoginRequest
import om.sanraksh.app.data.model.RegisterRequest
import om.sanraksh.app.data.model.SafetyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // ── Auth ──

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    // ── Elder Safety ──

    @POST("api/safety/check-in")
    suspend fun checkIn(
        @Header("Authorization") authorization: String
    ): Response<SafetyResponse>

    @POST("api/safety/sos")
    suspend fun triggerSOS(
        @Header("Authorization") authorization: String
    ): Response<SafetyResponse>

    // ── Family ──

    @POST("api/family/link-elder")
    suspend fun linkElder(
        @Header("Authorization") authorization: String,
        @Body request: LinkElderRequest
    ): Response<LinkElderResponse>

    @GET("api/family/elders")
    suspend fun getLinkedElders(
        @Header("Authorization") authorization: String
    ): Response<FamilyEldersResponse>

    @GET("api/safety/status/{elderId}")
    suspend fun getElderStatus(
        @Header("Authorization") authorization: String,
        @Path("elderId") elderId: String
    ): Response<ElderStatusResponse>

    @GET("api/safety/events/{elderId}")
    suspend fun getElderEvents(
        @Header("Authorization") authorization: String,
        @Path("elderId") elderId: String
    ): Response<ElderEventsResponse>
}