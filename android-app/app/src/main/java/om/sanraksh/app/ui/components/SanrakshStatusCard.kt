package om.sanraksh.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import om.sanraksh.app.ui.theme.*

enum class SafetyStatus {
    SAFE,
    EMERGENCY,
    NOT_CHECKED_IN
}

@Composable
fun SanrakshStatusCard(
    status: SafetyStatus,
    title: String,
    subtitle: String = "",
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val (containerColor, contentColor, iconDescription) = when (status) {
        SafetyStatus.SAFE -> Triple(SuccessLight, Success, "Safe status")
        SafetyStatus.EMERGENCY -> Triple(EmergencyLight, Emergency, "Emergency status")
        SafetyStatus.NOT_CHECKED_IN -> Triple(WarningLight, Warning, "Not checked in status")
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .semantics {
                contentDescription = "$iconDescription: $title. $subtitle"
            },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                fontSize = 24.sp
            )

            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun SanrakshSuccessBanner(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SuccessLight, MaterialTheme.shapes.medium)
            .padding(16.dp)
            .semantics {
                contentDescription = "Success: $message"
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = Success,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = SuccessDark
        )
    }
}
