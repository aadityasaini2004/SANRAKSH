package om.sanraksh.app.data.model

data class SafetyResponse(
    val success: Boolean,
    val message: String,
    val event: SafetyEvent?
)