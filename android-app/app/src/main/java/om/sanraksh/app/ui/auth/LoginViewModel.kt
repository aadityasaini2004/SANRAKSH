package om.sanraksh.app.ui.auth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import om.sanraksh.app.data.model.LoginRequest
import om.sanraksh.app.data.remote.RetrofitClient
import om.sanraksh.app.data.repository.AuthRepository

data class LoginUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val message: String = "",
    val role: String? = null
)

class LoginViewModel : ViewModel() {

    private val repository =
        AuthRepository(RetrofitClient.apiService)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {

            _uiState.value = LoginUiState(
                isLoading = true
            )

            val request = LoginRequest(
                email = email.trim(),
                password = password
            )

            val result = repository.login(request)

            result.onSuccess { response ->

                _uiState.value = LoginUiState(
                    isLoading = false,
                    success = response.success,
                    message = response.message,
                    role = response.user?.role
                )

            }.onFailure { error ->

                _uiState.value = LoginUiState(
                    isLoading = false,
                    success = false,
                    message = error.message ?: "Login failed"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}