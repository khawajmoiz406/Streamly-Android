package com.livestreaming.streamly.config.utils.extension

import android.net.Uri
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.json.Json

inline fun <reified T : Any> toNavType(nullable: Boolean = false): NavType<T?> {
    return object : NavType<T?>(isNullableAllowed = nullable) {
        override fun serializeAsValue(value: T?): String {
            return value?.let { Uri.encode(Json.encodeToString(it)) } ?: ""
        }

        override fun parseValue(value: String): T? {
            if (value.isBlank()) return null
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(bundle: SavedState, key: String, value: T?) {
            bundle.putString(key, value?.let { Json.encodeToString(it) } ?: "")
        }

        override fun get(bundle: SavedState, key: String): T? {
            val str = bundle.getString(key, "")
            if (str.isBlank()) return null
            return Json.decodeFromString(str)
        }
    }
}