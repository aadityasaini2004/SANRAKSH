package om.sanraksh.app.ui.elder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import om.sanraksh.app.ui.components.SanrakshButton
import om.sanraksh.app.ui.components.SanrakshSuccessBanner
import om.sanraksh.app.ui.theme.*

@Composable
fun ElderProfileScreen(
    sanrakshId: String?,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var showCopied by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                    text = "My Profile",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = OnBackground,
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ── Sanraksh ID Section ──
                Text(
                    text = "My Sanraksh ID",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = OnBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Share this ID with a trusted family member.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ── Sanraksh ID Display Card ──
                if (sanrakshId != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Surface, MaterialTheme.shapes.large)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Shield,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = sanrakshId,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Primary,
                            fontSize = 32.sp,
                            letterSpacing = 4.sp,
                            modifier = Modifier.semantics {
                                contentDescription = "My Sanraksh ID, $sanrakshId. Copy ID."
                            }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // ── Copy Button ──
                        SanrakshButton(
                            text = "Copy ID",
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Sanraksh ID", sanrakshId)
                                clipboard.setPrimaryClip(clip)
                                showCopied = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    // ── No ID Available ──
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Surface, MaterialTheme.shapes.large)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Sanraksh ID not available",
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Copy Success Feedback ──
                if (showCopied) {
                    SanrakshSuccessBanner(
                        message = "Sanraksh ID copied."
                    )
                }
            }
        }
    }
}
