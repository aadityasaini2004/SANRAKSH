package om.sanraksh.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import om.sanraksh.app.ui.auth.LoginScreen
import androidx.compose.material3.Text
import om.sanraksh.app.ui.auth.RegisterScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = { role ->

                    when (role.lowercase()) {

                        "elder" -> {
                            // Elder dashboard later
                        }

                        "family" -> {
                            // Family dashboard later
                        }
                    }
                }
            )
        }

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
    }
}