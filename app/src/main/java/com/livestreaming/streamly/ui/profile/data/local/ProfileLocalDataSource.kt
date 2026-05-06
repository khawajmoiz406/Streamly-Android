package com.livestreaming.streamly.ui.profile.data.local

import android.content.Context
import com.livestreaming.streamly.config.theme.ThemeMode
import com.livestreaming.streamly.core.pref.EncryptedSharedPref
import com.livestreaming.streamly.core.pref.SharedPrefKeys.APP_THEME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProfileLocalDataSource @Inject constructor(
    private val pref: EncryptedSharedPref,
    @param:ApplicationContext private val context: Context
) {
    fun clearUser() = pref.clearAll()

    fun changeThemeMode(themeMode: ThemeMode) {
        EncryptedSharedPref.getInstance(context).putInt(APP_THEME, themeMode.value)
    }
}
