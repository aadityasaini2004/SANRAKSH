package om.sanraksh.app.ui.elder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import om.sanraksh.app.data.repository.SafetyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ElderUiState(
    val isLoading: Boolean = false,
    val isEmergency: Boolean = false,
    val statusMessage: String = "You haven't checked in yet.",
    val lastCheckIn: String? = null,
    val errorMessage: String = ""
)

class ElderViewModel : ViewModel() {

    private val repository = SafetyRepository()

    private val _uiState = MutableStateFlow(ElderUiState())
    val uiState: StateFlow<ElderUiState> = _uiState.asStateFlow()

    fun checkIn(accessToken: String) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = ""
            )

            val result = repository.checkIn(accessToken)

            result.onSuccess { response ->

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEmergency = false,
                    statusMessage = response.message,
                    lastCheckIn = response.event?.createdAt
                )

            }.onFailure { error ->

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Check-in failed"
                )
            }
        }
    }

    fun triggerSOS(accessToken: String) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = ""
            )

            val result = repository.triggerSOS(accessToken)

            result.onSuccess { response ->

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEmergency = true,
                    statusMessage = response.message,
                    lastCheckIn = response.event?.createdAt
                )

            }.onFailure { error ->

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "SOS failed"
                )
            }
        }
    }
}