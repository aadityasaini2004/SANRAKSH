package om.sanraksh.app.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val preferences = EncryptedSharedPreferences.create(
        context,
        "sanraksh_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        preferences.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply()
    }

    fun getAccessToken(): String? {
        return preferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return preferences.getString(KEY_REFRESH_TOKEN, null)
    }

    fun clearTokens() {
        preferences.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
    }

    fun saveUser(userId: String, name: String, email: String, role: String, sanrakshId: String?) {
        preferences.edit()
            .putString(KEY_USER_ID, userId)
            .putString(KEY_USER_NAME, name)
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_USER_ROLE, role)
            .putString(KEY_USER_SANRAKSH_ID, sanrakshId)
            .apply()
    }

    fun getSavedUser(): SavedUser? {
        val id = preferences.getString(KEY_USER_ID, null) ?: return null
        val name = preferences.getString(KEY_USER_NAME, null) ?: return null
        val email = preferences.getString(KEY_USER_EMAIL, null) ?: return null
        val role = preferences.getString(KEY_USER_ROLE, null) ?: return null
        val sanrakshId = preferences.getString(KEY_USER_SANRAKSH_ID, null)
        return SavedUser(id, name, email, role, sanrakshId)
    }

    fun clearAll() {
        preferences.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return !getAccessToken().isNullOrBlank()
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_USER_SANRAKSH_ID = "user_sanraksh_id"
    }
}

data class SavedUser(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val sanrakshId: String?
)