package com.livestreaming.streamly.config.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    val corePermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
    )

    val legacyStoragePermissions
        get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else emptyArray()

    val allPermissions: Array<String>
        get() = corePermissions + legacyStoragePermissions

    fun isGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun areAllGranted(
        context: Context,
        allPermissions: Array<String> = PermissionUtils.allPermissions
    ): Boolean {
        return allPermissions.all { isGranted(context, it) }
    }

    fun getDeniedPermissions(
        context: Context,
        allPermissions: Array<String> = PermissionUtils.allPermissions
    ): Array<String> {
        return allPermissions.filter {
            !isGranted(context, it)
        }.toTypedArray()
    }

    fun isPermanentlyDenied(activity: Activity, permission: String): Boolean {
        return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                && !isGranted(activity, permission)
    }

    fun requestPermissions(
        permissions: Array<String>,
        launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ) {
        if (permissions.isNotEmpty()) launcher.launch(permissions)
    }

    fun requestPermission(launcher: ActivityResultLauncher<String>, permission: String) {
        launcher.launch(permission)
    }

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    fun handlePermissionResult(
        activity: Activity,
        result: Map<String, Boolean>,
        onGranted: () -> Unit,
        onDenied: () -> Unit,
        onPermanentlyDenied: () -> Unit
    ) {
        val deniedPermissions = result.filter { (_, granted) -> !granted }
        if (deniedPermissions.isEmpty()) return onGranted.invoke()
        val permanentlyDeniedPermissions = deniedPermissions.any { (permission, granted) ->
            !granted && isPermanentlyDenied(activity, permission)
        }

        if (permanentlyDeniedPermissions) return onPermanentlyDenied.invoke()
        return onDenied.invoke()
    }
}