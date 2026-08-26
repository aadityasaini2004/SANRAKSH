package om.sanraksh.app.data.model

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val user: User?,
    val token: TokenResponse?
)