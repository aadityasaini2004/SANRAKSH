package om.sanraksh.app.ui.family

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import om.sanraksh.app.data.model.ElderStatusResponse
import om.sanraksh.app.data.model.FamilyElder
import om.sanraksh.app.data.model.SafetyEvent
import om.sanraksh.app.data.remote.RetrofitClient
import om.sanraksh.app.data.repository.FamilyRepository

data class ElderWithStatus(
    val elder: FamilyElder,
    val status: ElderStatusResponse? = null,
    val isLoading: Boolean = false
)

data class FamilyUiState(
    val isLoading: Boolean = false,
    val elders: List<ElderWithStatus> = emptyList(),
    val selectedElder: FamilyElder? = null,
    val selectedElderEvents: List<SafetyEvent> = emptyList(),
    val isEventsLoading: Boolean = false,
    val errorMessage: String = "",
    val linkElderId: String = "",
    val isLinking: Boolean = false,
    val linkSuccess: Boolean = false,
    val linkError: String = ""
)

class FamilyViewModel : ViewModel() {

    private val repository = FamilyRepository()
    private val tokenManager = RetrofitClient.tokenManager

    private val _uiState = MutableStateFlow(FamilyUiState())
    val uiState: StateFlow<FamilyUiState> = _uiState.asStateFlow()

    init {
        loadElders()
    }

    fun loadElders() {
        val token = tokenManager.getAccessToken() ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = ""
            )

            val result = repository.getLinkedElders(token)

            result.onSuccess { response ->
                val elderList = response.elders.map { elder ->
                    ElderWithStatus(elder = elder)
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    elders = elderList
                )
                // Load status for each elder
                loadAllElderStatuses()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Failed to load elders"
                )
            }
        }
    }

    private fun loadAllElderStatuses() {
        val token = tokenManager.getAccessToken() ?: return
        val elders = _uiState.value.elders

        viewModelScope.launch {
            elders.forEach { elderWithStatus ->
                _uiState.value = _uiState.value.copy(
                    elders = _uiState.value.elders.map {
                        if (it.elder._id == elderWithStatus.elder._id) {
                            it.copy(isLoading = true)
                        } else it
                    }
                )

                val result = repository.getElderStatus(token, elderWithStatus.elder._id)
                result.onSuccess { statusResponse ->
                    _uiState.value = _uiState.value.copy(
                        elders = _uiState.value.elders.map {
                            if (it.elder._id == elderWithStatus.elder._id) {
                                it.copy(status = statusResponse, isLoading = false)
                            } else it
                        }
                    )
                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        elders = _uiState.value.elders.map {
                            if (it.elder._id == elderWithStatus.elder._id) {
                                it.copy(isLoading = false)
                            } else it
                        }
                    )
                }
            }
        }
    }

    fun loadElderEvents(elderId: String) {
        val token = tokenManager.getAccessToken() ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isEventsLoading = true)

            val result = repository.getElderEvents(token, elderId)
            result.onSuccess { response ->
                _uiState.value = _uiState.value.copy(
                    isEventsLoading = false,
                    selectedElderEvents = response.events
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isEventsLoading = false,
                    errorMessage = error.message ?: "Failed to load events"
                )
            }
        }
    }

    fun selectElder(elder: FamilyElder) {
        _uiState.value = _uiState.value.copy(selectedElder = elder)
        loadElderEvents(elder._id)
    }

    fun clearSelectedElder() {
        _uiState.value = _uiState.value.copy(
            selectedElder = null,
            selectedElderEvents = emptyList()
        )
    }

    fun updateLinkElderId(id: String) {
        _uiState.value = _uiState.value.copy(linkElderId = id)
    }

    fun linkElder() {
        val token = tokenManager.getAccessToken() ?: return
        val elderId = _uiState.value.linkElderId.trim()

        if (elderId.isEmpty()) {
            _uiState.value = _uiState.value.copy(linkError = "Please enter an Elder ID")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLinking = true,
                linkError = "",
                linkSuccess = false
            )

            val result = repository.linkElder(token, elderId)
            result.onSuccess { response ->
                _uiState.value = _uiState.value.copy(
                    isLinking = false,
                    linkSuccess = true,
                    linkElderId = ""
                )
                // Reload elders to include the newly linked one
                loadElders()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLinking = false,
                    linkError = error.message ?: "Failed to link elder"
                )
            }
        }
    }

    fun dismissLinkSheet() {
        _uiState.value = _uiState.value.copy(
            linkElderId = "",
            isLinking = false,
            linkSuccess = false,
            linkError = ""
        )
    }

    fun refreshAll() {
        loadElders()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }
}
