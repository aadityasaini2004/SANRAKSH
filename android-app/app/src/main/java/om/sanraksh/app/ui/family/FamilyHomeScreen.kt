package om.sanraksh.app.ui.family

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import om.sanraksh.app.data.model.FamilyElder
import om.sanraksh.app.data.model.SafetyEvent
import om.sanraksh.app.ui.components.SafetyStatus
import om.sanraksh.app.ui.components.SanrakshButton
import om.sanraksh.app.ui.components.SanrakshErrorCard
import om.sanraksh.app.ui.components.SanrakshStatusCard
import om.sanraksh.app.ui.components.SanrakshTopBar
import om.sanraksh.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyHomeScreen(
    onLogoutClick: () -> Unit,
    viewModel: FamilyViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showLinkSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        if (uiState.selectedElder != null) {
            ElderDetailView(
                elder = uiState.selectedElder!!,
                events = uiState.selectedElderEvents,
                isEventsLoading = uiState.isEventsLoading,
                onBack = { viewModel.clearSelectedElder() },
                onCall = { phone ->
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                    context.startActivity(intent)
                }
            )
        } else {
            MainDashboard(
                uiState = uiState,
                onLogoutClick = onLogoutClick,
                onRefresh = { viewModel.refreshAll() },
                onSelectElder = { viewModel.selectElder(it) },
                onOpenLinkSheet = { showLinkSheet = true },
                onClearError = { viewModel.clearError() }
            )
        }
    }

    // ── Link Elder Bottom Sheet ──
    if (showLinkSheet) {
        LinkElderSheet(
            uiState = uiState,
            sheetState = sheetState,
            onDismiss = {
                showLinkSheet = false
                viewModel.dismissLinkSheet()
            },
            onIdChange = { viewModel.updateLinkSanrakshId(it) },
            onLink = { viewModel.linkElder() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainDashboard(
    uiState: FamilyUiState,
    onLogoutClick: () -> Unit,
    onRefresh: () -> Unit,
    onSelectElder: (FamilyElder) -> Unit,
    onOpenLinkSheet: () -> Unit,
    onClearError: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SanrakshTopBar(onLogoutClick = onLogoutClick)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Greeting ──
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your family is in your care",
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceVariant
                    )
                }

                // ── Error ──
                if (uiState.errorMessage.isNotEmpty()) {
                    item {
                        SanrakshErrorCard(
                            message = uiState.errorMessage,
                            onRetry = {
                                onClearError()
                                onRefresh()
                            }
                        )
                    }
                }

                // ── Loading ──
                if (uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = Primary)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Loading your family members...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = OnSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // ── Empty State ──
                if (!uiState.isLoading && uiState.elders.isEmpty() && uiState.errorMessage.isEmpty()) {
                    item {
                        EmptyElderState()
                    }
                }

                // ── Elder List ──
                if (uiState.elders.isNotEmpty()) {
                    item {
                        Text(
                            text = "Linked Elders",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = OnBackground
                        )
                    }

                    items(uiState.elders) { elderWithStatus ->
                        ElderStatusCard(
                            elderWithStatus = elderWithStatus,
                            onClick = { onSelectElder(elderWithStatus.elder) }
                        )
                    }
                }

                // ── Bottom Spacer ──
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // ── FAB ──
        FloatingActionButton(
            onClick = onOpenLinkSheet,
            containerColor = Primary,
            contentColor = OnPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Link Elder"
            )
        }
    }
}

@Composable
private fun ElderStatusCard(
    elderWithStatus: ElderWithStatus,
    onClick: () -> Unit
) {
    val elder = elderWithStatus.elder
    val statusResponse = elderWithStatus.status

    val (status, statusTitle, statusSubtitle, statusIcon) = when {
        elderWithStatus.isLoading -> Quadruple(
            SafetyStatus.NOT_CHECKED_IN,
            "Loading...",
            "",
            Icons.Filled.Shield
        )
        statusResponse?.status == "EMERGENCY" -> Quadruple(
            SafetyStatus.EMERGENCY,
            "Emergency",
            statusResponse.lastEvent?.let { "SOS triggered" } ?: "Action required",
            Icons.Filled.Warning
        )
        statusResponse?.status == "SAFE" -> Quadruple(
            SafetyStatus.SAFE,
            "Safe",
            statusResponse.lastEvent?.let { "Checked in" } ?: "Safe",
            Icons.Filled.Shield
        )
        else -> Quadruple(
            SafetyStatus.NOT_CHECKED_IN,
            "Not Checked In",
            "No check-ins yet",
            Icons.Filled.Warning
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = "${elder.name}, $statusTitle. $statusSubtitle"
            }
    ) {
        // Elder name header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface, MaterialTheme.shapes.large)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Shield,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = elder.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = OnBackground
                )
                if (elder.phoneNumber.isNotEmpty()) {
                    Text(
                        text = elder.phoneNumber,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant
                    )
                }
            }
            Icon(
                imageVector = Icons.Filled.Phone,
                contentDescription = "Call ${elder.name}",
                tint = Primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Status card
        SanrakshStatusCard(
            status = status,
            title = statusTitle,
            subtitle = statusSubtitle,
            icon = statusIcon
        )
    }
}

@Composable
private fun ElderDetailView(
    elder: FamilyElder,
    events: List<SafetyEvent>,
    isEventsLoading: Boolean,
    onBack: () -> Unit,
    onCall: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        // ── Top Bar with Back ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = OnSurface
                )
            }
            Text(
                text = elder.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = OnBackground,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onCall(elder.phoneNumber) }) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "Call ${elder.name}",
                    tint = Primary
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Contact Info ──
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = elder.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
            }

            // ── Event History ──
            item {
                Text(
                    text = "Safety Events",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = OnBackground
                )
            }

            if (isEventsLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Primary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Loading safety history...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
            }

            if (!isEventsLoading && events.isEmpty()) {
                item {
                    Text(
                        text = "No safety events yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceVariant,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }

            items(events) { event ->
                EventCard(event = event)
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun EventCard(event: SafetyEvent) {
    val isEmergency = event.type == "SOS"
    val bgColor = if (isEmergency) EmergencyLight else SuccessLight
    val iconTint = if (isEmergency) Emergency else Success
    val icon = if (isEmergency) Icons.Filled.Warning else Icons.Filled.CheckCircle
    val title = if (isEmergency) "SOS Emergency" else "Safety Check-In"
    val description = if (isEmergency) "Emergency alert sent" else "Confirmed safe"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, MaterialTheme.shapes.medium)
            .padding(16.dp)
            .semantics {
                contentDescription = "$title. $description. ${event.createdAt}"
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = OnBackground
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant
            )
        }
        Text(
            text = event.createdAt,
            style = MaterialTheme.typography.labelMedium,
            color = OnSurfaceVariant
        )
    }
}

@Composable
private fun EmptyElderState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Shield,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Primary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No Elders Linked Yet",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = OnBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Connect an elderly family member\nto start monitoring their safety.",
            style = MaterialTheme.typography.bodyLarge,
            color = OnSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LinkElderSheet(
    uiState: FamilyUiState,
    sheetState: androidx.compose.material3.SheetState,
    onDismiss: () -> Unit,
    onIdChange: (String) -> Unit,
    onLink: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Surface,
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Link an Elder",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = OnBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter the Sanraksh ID shared by the elder.",
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.linkSanrakshId,
                onValueChange = onIdChange,
                label = { Text("Sanraksh ID") },
                placeholder = { Text("e.g. SNR-7K4P92") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Outline,
                    cursorColor = Primary
                ),
                shape = MaterialTheme.shapes.medium
            )

            if (uiState.linkError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.linkError,
                    color = ErrorRed,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (uiState.linkSuccess) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Elder linked successfully!",
                    color = Success,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SanrakshButton(
                text = "Link Elder",
                onClick = onLink,
                isLoading = uiState.isLinking,
                loadingText = "Linking...",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Helper data class for 4-element destructuring
private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
