package om.sanraksh.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import om.sanraksh.app.data.remote.RetrofitClient
import om.sanraksh.app.ui.auth.LoginScreen
import om.sanraksh.app.ui.auth.RegisterScreen
import om.sanraksh.app.ui.elder.ElderHomeScreen
import om.sanraksh.app.ui.theme.*

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val ELDER_HOME = "elder_home"
    const val FAMILY_HOME = "family_home"
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // ---------------- LOGIN ----------------

        composable(Routes.LOGIN) {

            LoginScreen(

                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                },

                onLoginSuccess = { role ->

                    when (role.lowercase()) {

                        "elder" -> {
                            navController.navigate(Routes.ELDER_HOME) {
                                popUpTo(Routes.LOGIN) {
                                    inclusive = true
                                }
                            }
                        }

                        "family" -> {
                            navController.navigate(Routes.FAMILY_HOME) {
                                popUpTo(Routes.LOGIN) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            )
        }

        // ---------------- REGISTER ----------------

        composable(Routes.REGISTER) {

            RegisterScreen(
                onRegistrationSuccess = {

                    navController.navigate(Routes.LOGIN) {

                        popUpTo(Routes.REGISTER) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // ---------------- ELDER HOME ----------------

        composable(Routes.ELDER_HOME) {

            val accessToken =
                RetrofitClient.tokenManager.getAccessToken()

            if (!accessToken.isNullOrBlank()) {

                ElderHomeScreen(
                    accessToken = accessToken,
                    onLogoutClick = {
                        RetrofitClient.tokenManager.clearTokens()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )

            } else {

                LaunchedEffect(Unit) {

                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }
        }

        // ---------------- FAMILY HOME ----------------

        composable(Routes.FAMILY_HOME) {

            val accessToken =
                RetrofitClient.tokenManager.getAccessToken()

            if (!accessToken.isNullOrBlank()) {

                FamilyHomePlaceholder(
                    onLogoutClick = {
                        RetrofitClient.tokenManager.clearTokens()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )

            } else {

                LaunchedEffect(Unit) {

                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FamilyHomePlaceholder(
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        om.sanraksh.app.ui.components.SanrakshTopBar(
            onLogoutClick = onLogoutClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Construction,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Family Dashboard",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = OnBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Coming Soon",
                style = MaterialTheme.typography.titleLarge,
                color = OnSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Family monitoring features are under development.\nCheck back soon for updates.",
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}