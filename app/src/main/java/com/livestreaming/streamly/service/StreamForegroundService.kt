package com.livestreaming.streamly.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.livestream.streamly.R
import com.livestreaming.streamly.MainActivity
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.ui.broadcast.domain.usecase.EndStreamUseCase
import com.livestreaming.streamly.ui.watch.data.remote.dto.StreamUserRequest
import com.livestreaming.streamly.ui.watch.domain.usecase.LeaveStreamUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StreamForegroundService : Service() {
    private var isBroadcaster: Boolean = false
    private lateinit var stream: Stream
    private lateinit var user: User

    @Inject
    lateinit var endStreamUseCase: EndStreamUseCase

    @Inject
    lateinit var leaveStreamUseCase: LeaveStreamUseCase

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val CHANNEL_ID = "stream_channel"

        fun start(context: Context, isBroadcaster: Boolean, stream: Stream, user: User) =
            Intent(context, StreamForegroundService::class.java).apply {
                action = ACTION_START
                putExtra("EXTRA_IS_BROADCASTER", isBroadcaster)
                putExtra("EXTRA_STREAM", stream)
                putExtra("EXTRA_USER", user)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(this)
                } else {
                    context.startService(this)
                }
            }

        fun stop(context: Context?) = Intent(context, StreamForegroundService::class.java).apply {
            context?.stopService(this)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        when (action) {
            ACTION_START -> initForegroundService(intent)
            ACTION_STOP -> stopSelf()
        }

        return START_STICKY
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        GlobalScope.launch(Dispatchers.IO + NonCancellable) {
            try {
                if (isBroadcaster) {
                    endStreamUseCase.invoke(stream)
                } else {
                    leaveStreamUseCase.invoke(StreamUserRequest(stream, user))
                }
            } catch (e: Exception) {
                Log.e("StreamService", "onTaskRemoved: ${e.message}")
            } finally {
                stopSelf()
            }
        }
    }

    private fun initForegroundService(intent: Intent?) {
        intent?.let {
            isBroadcaster = it.getBooleanExtra("EXTRA_IS_BROADCASTER", false)
            stream = it.getSerializableExtra("EXTRA_STREAM", Stream::class.java) as Stream
            user = it.getSerializableExtra("EXTRA_USER", User::class.java) as User

            val notification = buildNotification(isBroadcaster)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                startForeground(
                    101,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA or
                            ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
                )
            } else {
                startForeground(101, notification)
            }
        } ?: stopSelf()
    }

    private fun buildNotification(isBroadcaster: Boolean): Notification {
        createNotificationChannel()

        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val endStreamIntent = Intent(this, StreamForegroundService::class.java).apply {
            action = ACTION_STOP
        }
        val endPendingIntent = PendingIntent.getService(
            this, 1, endStreamIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(if (isBroadcaster) "You are live" else "Watching stream")
            .setContentText("Tap to return to stream")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_end,
                if (isBroadcaster) "End Stream" else "Leave Stream",
                endPendingIntent
            )
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Live Stream",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?) = null
}