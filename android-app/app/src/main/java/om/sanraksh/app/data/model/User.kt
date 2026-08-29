package om.sanraksh.app.data.model

data class User(
    val _id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val role: String,
    val sanrakshId: String? = null
)