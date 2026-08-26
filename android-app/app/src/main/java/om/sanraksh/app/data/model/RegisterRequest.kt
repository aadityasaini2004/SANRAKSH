package om.sanraksh.app.data.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val role: String
)