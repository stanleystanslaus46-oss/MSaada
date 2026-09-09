package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("msaada_app_preferences", Context.MODE_PRIVATE)

    private val _onboardingCompleted =
        MutableStateFlow(prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false))
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

    private val _language = MutableStateFlow(prefs.getString(KEY_LANGUAGE, "sw") ?: "sw")
    val language: StateFlow<String> = _language.asStateFlow()

    private val _themeMode = MutableStateFlow(prefs.getString(KEY_THEME, "light") ?: "light")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _notificationsEnabled =
        MutableStateFlow(prefs.getBoolean(KEY_NOTIFICATIONS, true))
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _passportPhotosUsed =
        MutableStateFlow(prefs.getInt(KEY_PASSPORT_PHOTOS_USED, 0))
    val passportPhotosUsed: StateFlow<Int> = _passportPhotosUsed.asStateFlow()

    private val _isProUser = MutableStateFlow(prefs.getBoolean(KEY_IS_PRO, false))
    private val _proExpiryTime = MutableStateFlow(prefs.getLong(KEY_PRO_EXPIRY_TIME, 0L))
    val isProUser: StateFlow<Boolean> = _isProUser.asStateFlow()
    val proExpiryTime: StateFlow<Long> = _proExpiryTime.asStateFlow()

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
        _onboardingCompleted.value = completed
    }

    fun setLanguage(lang: String) {
        prefs.edit().putString(KEY_LANGUAGE, lang).apply()
        _language.value = lang
    }

    fun setThemeMode(mode: String) {
        prefs.edit().putString(KEY_THEME, mode).apply()
        _themeMode.value = mode
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply()
        _notificationsEnabled.value = enabled
    }

    fun incrementPassportPhotosUsed(): Int {
        val next = prefs.getInt(KEY_PASSPORT_PHOTOS_USED, 0) + 1
        prefs.edit().putInt(KEY_PASSPORT_PHOTOS_USED, next).apply()
        _passportPhotosUsed.value = next
        return next
    }

    fun setIsProUser(isPro: Boolean) {
        prefs.edit().putBoolean(KEY_IS_PRO, isPro).apply()
        _isProUser.value = isPro
    }

    fun setVerifiedProEntitlement(isPro: Boolean, expiryTimeMillis: Long = 0L) {
        prefs.edit()
            .putBoolean(KEY_IS_PRO, isPro)
            .putLong(KEY_PRO_EXPIRY_TIME, expiryTimeMillis)
            .apply()
        _isProUser.value = isPro
        _proExpiryTime.value = expiryTimeMillis
    }

    fun clearAll() {
        prefs.edit().clear().apply()
        _onboardingCompleted.value = false
        _language.value = "sw"
        _themeMode.value = "light"
        _notificationsEnabled.value = true
        _passportPhotosUsed.value = 0
        _isProUser.value = false
        _proExpiryTime.value = 0L
    }

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "key_onboarding_completed"
        private const val KEY_LANGUAGE = "key_language"
        private const val KEY_THEME = "key_theme"
        private const val KEY_NOTIFICATIONS = "key_notifications"
        private const val KEY_PASSPORT_PHOTOS_USED = "key_passport_photos_used"
        private const val KEY_IS_PRO = "key_is_pro"
        private const val KEY_PRO_EXPIRY_TIME = "key_pro_expiry_time"
    }
}
