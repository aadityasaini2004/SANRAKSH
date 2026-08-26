package om.sanraksh.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import om.sanraksh.app.data.model.RegisterRequest
import om.sanraksh.app.data.remote.RetrofitClient
import om.sanraksh.app.data.repository.AuthRepository

data class RegisterUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val message: String = ""
)

class RegisterViewModel : ViewModel() {

    private val repository =
        AuthRepository(RetrofitClient.apiService)

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        role: String
    ) {
        viewModelScope.launch {

            _uiState.value = RegisterUiState(
                isLoading = true
            )

            val request = RegisterRequest(
                name = name.trim(),
                email = email.trim(),
                password = password,
                phoneNumber = phoneNumber.trim(),
                role = role
            )

            val result = repository.register(request)

            result.onSuccess { response ->

                _uiState.value = RegisterUiState(
                    isLoading = false,
                    success = response.success,
                    message = response.message
                )

            }.onFailure { error ->

                _uiState.value = RegisterUiState(
                    isLoading = false,
                    success = false,
                    message = error.message ?: "Registration failed"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState()
    }
}