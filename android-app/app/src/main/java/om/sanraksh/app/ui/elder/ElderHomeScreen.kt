package om.sanraksh.app.ui.elder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import om.sanraksh.app.ui.components.SafetyStatus
import om.sanraksh.app.ui.components.SanrakshButton
import om.sanraksh.app.ui.components.SanrakshConfirmDialog
import om.sanraksh.app.ui.components.SanrakshEmergencyButton
import om.sanraksh.app.ui.components.SanrakshErrorCard
import om.sanraksh.app.ui.components.SanrakshStatusCard
import om.sanraksh.app.ui.components.SanrakshSuccessBanner
import om.sanraksh.app.ui.components.SanrakshTopBar
import om.sanraksh.app.ui.theme.*

@Composable
fun ElderHomeScreen(
    accessToken: String,
    onLogoutClick: () -> Unit = {},
    viewModel: ElderViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    var showSosDialog by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var previousEmergencyState by remember { mutableStateOf(false) }

    // Detect check-in success (transition from emergency or initial to safe)
    if (!uiState.isEmergency && uiState.statusMessage != "You haven't checked in yet." && uiState.errorMessage.isEmpty() && !uiState.isLoading) {
        if (!previousEmergencyState && !showSuccess) {
            showSuccess = true
        }
    }
    previousEmergencyState = uiState.isEmergency

    // SOS Confirmation Dialog
    if (showSosDialog) {
        SanrakshConfirmDialog(
            title = "Send Emergency Alert?",
            message = "Are you sure you need help? If you confirm, your emergency alert will be sent to your contacts.",
            confirmText = "Yes, Send SOS",
            cancelText = "Cancel",
            isDestructive = true,
            onConfirm = {
                viewModel.triggerSOS(accessToken)
            },
            onDismiss = {
                showSosDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ── Top Bar ──
            SanrakshTopBar(
                onLogoutClick = onLogoutClick
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ── Greeting ──
                Text(
                    text = "Good morning \uD83D\uDC4B",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = OnBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "How are you today?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Status Card ──
                val statusResult = when {
                    uiState.isEmergency -> Triple(
                        SafetyStatus.EMERGENCY,
                        "Emergency Active",
                        Icons.Filled.Warning
                    )
                    uiState.statusMessage != "You haven't checked in yet." && uiState.errorMessage.isEmpty() -> Triple(
                        SafetyStatus.SAFE,
                        "You're Safe",
                        Icons.Filled.Shield
                    )
                    else -> Triple(
                        SafetyStatus.NOT_CHECKED_IN,
                        "Not Checked In",
                        Icons.Filled.Warning
                    )
                }
                val status = statusResult.first
                val statusTitle = statusResult.second
                val statusIcon = statusResult.third

                val statusSubtitle = when {
                    uiState.isEmergency -> "Help is on the way"
                    uiState.lastCheckIn != null -> "Last checked: ${uiState.lastCheckIn}"
                    else -> "Tap 'I Am Safe' below"
                }

                SanrakshStatusCard(
                    status = status,
                    title = statusTitle,
                    subtitle = statusSubtitle,
                    icon = statusIcon
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Success Feedback ──
                if (showSuccess) {
                    SanrakshSuccessBanner(
                        message = "Check-in successful! Your contacts have been notified."
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // ── Check-In Button ──
                SanrakshButton(
                    text = "I AM SAFE",
                    onClick = {
                        showSuccess = false
                        viewModel.checkIn(accessToken)
                    },
                    isLoading = uiState.isLoading,
                    loadingText = "Checking your safety...",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ── Divider ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(OutlineVariant)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Emergency Section ──
                Text(
                    text = "Need immediate help?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                SanrakshEmergencyButton(
                    text = "\uD83D\uDD34 SOS",
                    onClick = {
                        showSosDialog = true
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Error ──
                if (uiState.errorMessage.isNotEmpty()) {
                    SanrakshErrorCard(
                        message = uiState.errorMessage,
                        onRetry = {
                            viewModel.checkIn(accessToken)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}