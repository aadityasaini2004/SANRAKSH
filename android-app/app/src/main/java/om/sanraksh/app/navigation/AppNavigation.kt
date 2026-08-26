package om.sanraksh.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import om.sanraksh.app.data.remote.RetrofitClient
import om.sanraksh.app.ui.auth.LoginScreen
import om.sanraksh.app.ui.auth.RegisterScreen
import om.sanraksh.app.ui.elder.ElderHomeScreen

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
                    accessToken = accessToken
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

            Text(
                text = "Family Dashboard - Coming Soon"
            )
        }
    }
}