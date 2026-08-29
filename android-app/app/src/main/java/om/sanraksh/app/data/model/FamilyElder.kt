package om.sanraksh.app.data.model

// ── Request models ──

data class LinkElderRequest(
    val sanrakshId: String
)

// ── Response models ──

data class FamilyElder(
    val _id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val avatar: String?,
    val role: String,
    val sanrakshId: String? = null
)

data class FamilyEldersResponse(
    val success: Boolean,
    val elders: List<FamilyElder>
)

data class ElderStatusResponse(
    val success: Boolean,
    val status: String,
    val elder: ElderInfo,
    val lastEvent: SafetyEvent?
)

data class ElderInfo(
    val _id: String,
    val name: String,
    val avatar: String?
)

data class ElderEventsResponse(
    val success: Boolean,
    val events: List<SafetyEvent>
)

data class LinkElderResponse(
    val success: Boolean,
    val message: String,
    val elder: FamilyElder?
)
