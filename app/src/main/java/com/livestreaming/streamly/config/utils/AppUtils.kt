package com.livestreaming.streamly.config.utils

import android.content.Context
import android.content.res.Configuration
import com.google.gson.reflect.TypeToken
import com.livestreaming.streamly.config.theme.ThemeMode
import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.core.pref.EncryptedSharedPref
import com.livestreaming.streamly.core.pref.SharedPrefKeys.APP_THEME

object AppUtils {
    fun getCurrentUser(context: Context): User? {
        return EncryptedSharedPref.getInstance(context).getModel(object : TypeToken<User>() {})
    }

    fun isDarkTheme(context: Context): Boolean {
        val appThemeValue = EncryptedSharedPref.getInstance(context).getInt(APP_THEME)
        val theme = ThemeMode.fromValue(appThemeValue) ?: return isSystemInDarkTheme(context)
        return theme == ThemeMode.Dark
    }

    fun isSystemInDarkTheme(context: Context): Boolean {
        return context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}