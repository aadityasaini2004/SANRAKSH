package om.sanraksh.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import om.sanraksh.app.ui.components.SanrakshButton
import om.sanraksh.app.ui.components.SanrakshErrorCard
import om.sanraksh.app.ui.components.SanrakshSecondaryButton
import om.sanraksh.app.ui.components.SanrakshTextField
import om.sanraksh.app.ui.theme.*

@Composable
fun RegisterScreen(
    onRegistrationSuccess: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("elder") }
    var passwordMismatch by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            onRegistrationSuccess()
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // ── Header ──
            Icon(
                imageVector = Icons.Filled.Shield,
                contentDescription = "Sanraksh logo",
                modifier = Modifier.size(56.dp),
                tint = Primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = OnBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Join Sanraksh to stay connected and safe",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Personal Information Section ──
            Text(
                text = "Personal Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = OnBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SanrakshTextField(
                value = name,
                onValueChange = { name = it },
                label = "Full Name",
                leadingIcon = Icons.Filled.Person
            )

            Spacer(modifier = Modifier.height(12.dp))

            SanrakshTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Filled.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(12.dp))

            SanrakshTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Phone Number",
                leadingIcon = Icons.Filled.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Security Section ──
            Text(
                text = "Security",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = OnBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SanrakshTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordMismatch = false
                },
                label = "Password",
                leadingIcon = Icons.Filled.Lock,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            SanrakshTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    passwordMismatch = false
                },
                label = "Confirm Password",
                leadingIcon = Icons.Filled.Lock,
                isPassword = true,
                isError = passwordMismatch,
                errorMessage = if (passwordMismatch) "Passwords do not match" else ""
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Role Selection ──
            Text(
                text = "I am a...",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = OnBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Elder card
            RoleSelectionCard(
                icon = Icons.Filled.AccountCircle,
                title = "I am an Elder",
                subtitle = "I use Sanraksh myself",
                isSelected = role == "elder",
                onClick = { role = "elder" }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Family card
            RoleSelectionCard(
                icon = Icons.Filled.Group,
                title = "I am Family / Caregiver",
                subtitle = "I care for someone",
                isSelected = role == "family",
                onClick = { role = "family" }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Error ──
            if (uiState.message.isNotEmpty()) {
                SanrakshErrorCard(
                    message = uiState.message
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Submit ──
            SanrakshButton(
                text = "Create Account",
                onClick = {
                    if (password != confirmPassword) {
                        passwordMismatch = true
                        return@SanrakshButton
                    }
                    viewModel.register(
                        name = name,
                        email = email,
                        password = password,
                        phoneNumber = phoneNumber,
                        role = role
                    )
                },
                isLoading = uiState.isLoading,
                loadingText = "Creating account...",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Login link ──
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            SanrakshSecondaryButton(
                text = "Sign In",
                onClick = onRegistrationSuccess,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun RoleSelectionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Primary else Outline
    val bgColor = if (isSelected) PrimaryLight else Surface
    val iconTint = if (isSelected) Primary else OnSurfaceVariant

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(2.dp, borderColor, MaterialTheme.shapes.medium)
            .background(bgColor, MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = "$title. $subtitle. ${if (isSelected) "Selected" else "Not selected"}"
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = OnBackground
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant
            )
        }
    }
}