package com.livestreaming.streamly.config.utils

import android.app.Activity
import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Rational
import androidx.annotation.RequiresApi
import com.livestream.streamly.R

object PictureInPictureUtils {
    const val ACTION_TOGGLE_MIC = "com.livestream.streamly.ACTION_TOGGLE_MIC"
    const val ACTION_MUTE_CAMERA = "com.livestream.streamly.ACTION_MUTE_CAMERA"
    const val ACTION_LEAVE_CHANNEL = "com.livestream.streamly.ACTION_LEAVE_CHANNEL"
    const val ACTION_END_STREAM = "com.livestream.streamly.ACTION_END_STREAM"
    const val REQUEST_MIC = 100
    const val REQUEST_CAMERA = 101
    const val REQUEST_LEAVE_CHANNEL = 102
    const val REQUEST_END_STREAM = 103

    fun enterPipMode(
        activity: Activity,
        isMuted: Boolean,
        isCameraMute: Boolean,
        isBroadcaster: Boolean
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val params = PictureInPictureParams.Builder()
            .setAspectRatio(Rational(9, 16))
            .setActions(buildActions(activity, isMuted, isCameraMute, isBroadcaster))
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    setAutoEnterEnabled(false)
                }
            }
            .build()

        activity.enterPictureInPictureMode(params)
    }

    fun updatePipActions(
        activity: Activity,
        isMuted: Boolean,
        isCameraMute: Boolean,
        isBroadcaster: Boolean
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val params = PictureInPictureParams.Builder()
            .setActions(buildActions(activity, isMuted, isCameraMute, isBroadcaster))
            .build()

        activity.setPictureInPictureParams(params)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildActions(
        context: Context,
        isMuted: Boolean,
        isCameraMute: Boolean,
        isBroadcaster: Boolean
    ): List<RemoteAction> {
        val actions = mutableListOf<RemoteAction>()

        if (isBroadcaster) {
            val micIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_MIC,
                Intent(ACTION_TOGGLE_MIC).apply { `package` = context.packageName },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val cameraIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CAMERA,
                Intent(ACTION_MUTE_CAMERA).apply { `package` = context.packageName },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val muteIcon = if (isMuted) R.drawable.ic_microphone_disabled else R.drawable.ic_microphone
            val cameraIcon = if (isCameraMute) R.drawable.ic_camera_disabled else R.drawable.ic_camera

            actions.add(
                RemoteAction(
                    Icon.createWithResource(context, muteIcon),
                    if (isMuted) "Unmute" else "Mute",
                    if (isMuted) "Unmute microphone" else "Mute microphone",
                    micIntent
                )
            )

            actions.add(
                RemoteAction(
                    Icon.createWithResource(context, cameraIcon),
                    if (isCameraMute) "Enable camera" else "Disable camera",
                    if (isCameraMute) "Enable camera" else "Disable camera",
                    cameraIntent
                )
            )
        }

        return actions
    }
}