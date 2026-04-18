package com.livestreaming.streamly.ui.auth.data.local

import com.google.gson.reflect.TypeToken
import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.core.pref.EncryptedSharedPref
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(private val pref: EncryptedSharedPref) {
    fun saveUserModel(user: User?) {
        if (user == null) throw Exception("User cannot be null before saving to shared pref")
        pref.putModel(user, object : TypeToken<User>() {})
    }
}