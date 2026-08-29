package om.sanraksh.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import om.sanraksh.app.data.remote.RetrofitClient
import om.sanraksh.app.ui.auth.LoginScreen
import om.sanraksh.app.ui.auth.RegisterScreen
import om.sanraksh.app.ui.elder.ElderHomeScreen
import om.sanraksh.app.ui.elder.ElderProfileScreen
import om.sanraksh.app.ui.family.FamilyHomeScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val ELDER_HOME = "elder_home"
    const val ELDER_PROFILE = "elder_profile"
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
                        RetrofitClient.tokenManager.clearAll()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    },
                    onProfileClick = {
                        navController.navigate(Routes.ELDER_PROFILE)
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

        // ---------------- ELDER PROFILE ----------------

        composable(Routes.ELDER_PROFILE) {

            val user = RetrofitClient.tokenManager.getSavedUser()

            ElderProfileScreen(
                sanrakshId = user?.sanrakshId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ---------------- FAMILY HOME ----------------

        composable(Routes.FAMILY_HOME) {

            val accessToken =
                RetrofitClient.tokenManager.getAccessToken()

            if (!accessToken.isNullOrBlank()) {

                FamilyHomeScreen(
                    onLogoutClick = {
                        RetrofitClient.tokenManager.clearAll()
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