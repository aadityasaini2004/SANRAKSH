package om.sanraksh.app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import om.sanraksh.app.ui.components.SanrakshButton
import om.sanraksh.app.ui.components.SanrakshErrorCard
import om.sanraksh.app.ui.components.SanrakshSecondaryButton
import om.sanraksh.app.ui.components.SanrakshTextField
import om.sanraksh.app.ui.theme.*

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.success, uiState.role) {
        if (uiState.success && uiState.role != null) {
            onLoginSuccess(uiState.role!!)
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ── Branding ──
            Icon(
                imageVector = Icons.Filled.Shield,
                contentDescription = "Sanraksh logo",
                modifier = Modifier.size(72.dp),
                tint = Primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SANRAKSH",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = Primary,
                fontSize = 32.sp,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Stay Safe, Stay Connected",
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ── Form ──
            SanrakshTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Filled.Email,
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            SanrakshTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Filled.Lock,
                isPassword = true
            )

            // ── Error ──
            if (uiState.message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                SanrakshErrorCard(
                    message = uiState.message
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Sign In ──
            SanrakshButton(
                text = "Sign In",
                onClick = {
                    viewModel.login(
                        email = email,
                        password = password
                    )
                },
                isLoading = uiState.isLoading,
                loadingText = "Signing in...",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            SanrakshSecondaryButton(
                text = "Forgot Password?",
                onClick = { /* TODO: implement forgot password */ },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Register link ──
            Text(
                text = "Don't have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            SanrakshSecondaryButton(
                text = "Create Account",
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}