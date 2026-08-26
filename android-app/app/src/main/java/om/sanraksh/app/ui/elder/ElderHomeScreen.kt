package om.sanraksh.app.ui.elder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ElderHomeScreen(
    accessToken: String,
    viewModel: ElderViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "SANRAKSH"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = if (uiState.isEmergency) {
                "EMERGENCY"
            } else {
                "SAFE"
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = uiState.statusMessage
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {
                viewModel.checkIn(accessToken)
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("I AM SAFE")
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedButton(
            onClick = {
                viewModel.triggerSOS(accessToken)
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("SOS / EMERGENCY")
        }

        if (uiState.errorMessage.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = uiState.errorMessage
            )
        }
    }
}