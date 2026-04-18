package com.livestreaming.streamly.core.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Suppress("DEPRECATION")
class EncryptedSharedPref private constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: EncryptedSharedPref? = null

        fun getInstance(context: Context): EncryptedSharedPref {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: EncryptedSharedPref(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "${context.packageName}_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun <T> putModel(value: T?, typeToken: TypeToken<T>) {
        val key = typeToken.type.toString()
        if (value == null) {
            prefs.edit { remove(key) }
        } else {
            val json = Gson().toJson(value)
            prefs.edit { putString(key, json) }
        }
    }

    fun <T> getModel(typeToken: TypeToken<T>): T? {
        val key = typeToken.type.toString()
        val json = prefs.getString(key, null) ?: return null
        return Gson().fromJson<T>(json, typeToken.type)
    }

    fun removeModel(typeToken: TypeToken<*>) {
        prefs.edit { remove(typeToken.type.toString()) }
    }

    fun putInt(key: String, value: Int) {
        prefs.edit { putInt(key, value) }
    }

    fun getInt(key: String): Int {
        return prefs.getInt(key, -1)
    }

    fun clearAll() {
        prefs.edit { clear() }
    }
}
