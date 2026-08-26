package om.sanraksh.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import om.sanraksh.app.data.remote.RetrofitClient
import om.sanraksh.app.navigation.AppNavigation
import om.sanraksh.app.ui.theme.SanrakshTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitClient.initialize(applicationContext)

        setContent {
            SanrakshTheme {
                AppNavigation()
            }
        }
    }
}