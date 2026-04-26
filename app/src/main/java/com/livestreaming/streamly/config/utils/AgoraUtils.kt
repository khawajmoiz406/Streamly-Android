package com.livestreaming.streamly.config.utils

import android.content.Context
import android.view.SurfaceView
import com.livestreaming.streamly.config.utils.Constants.AGORA_APP_ID
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas

class AgoraManager(private val context: Context) {
    private var engine: RtcEngine? = null
    var onJoinSuccess: (() -> Unit)? = null
    var onRemoteUserJoined: ((uid: Int) -> Unit)? = null
    var onRemoteUserLeft: (() -> Unit)? = null
    var onError: ((message: String) -> Unit)? = null

    fun initialize() {
        try {
            val config = RtcEngineConfig().apply {
                mAppId = AGORA_APP_ID
                mContext = this@AgoraManager.context
                mEventHandler = buildEventHandler()
            }
            engine = RtcEngine.create(config)
        } catch (e: Exception) {
            onError?.invoke("Failed to init Agora: ${e.message}")
        }
    }

    fun startPreview(localView: SurfaceView) {
        engine?.apply {
            enableVideo()
            setChannelProfile(io.agora.rtc2.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
            setClientRole(io.agora.rtc2.Constants.CLIENT_ROLE_BROADCASTER)
            setupLocalVideo(VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, 0))
            startPreview()
        }
    }

    fun setupRemoteVideo(uid: Int, remoteView: SurfaceView) {
        engine?.setupRemoteVideo(
            VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid)
        )
    }

    fun goLiveOnChannel(channelName: String) {
        engine?.apply {
            val options = ChannelMediaOptions().apply {
                channelProfile = io.agora.rtc2.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
                clientRoleType = io.agora.rtc2.Constants.CLIENT_ROLE_BROADCASTER
                publishCameraTrack = true
                publishMicrophoneTrack = true
            }
            joinChannel(null, channelName, 0, options)
        }
    }

    fun joinAsViewer(channelName: String) {
        engine?.apply {
            enableVideo()
            setChannelProfile(io.agora.rtc2.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
            setClientRole(io.agora.rtc2.Constants.CLIENT_ROLE_AUDIENCE)

            val options = ChannelMediaOptions().apply {
                channelProfile = io.agora.rtc2.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
                clientRoleType = io.agora.rtc2.Constants.CLIENT_ROLE_AUDIENCE
                publishCameraTrack = false
                publishMicrophoneTrack = false
            }
            joinChannel(null, channelName, 0, options)
        }
    }

    fun leaveAsViewer() {
        engine?.leaveChannel()
    }

    fun switchCamera() {
        engine?.switchCamera()
    }

    fun toggleMic(muted: Boolean) {
        engine?.muteLocalAudioStream(muted)
    }

    fun stopPreview() {
        engine?.stopPreview()
    }

    fun stopBroadcast() {
        engine?.apply {
            stopPreview()
            leaveChannel()
        }
    }

    fun destroy() {
        engine?.leaveChannel()
        RtcEngine.destroy()
        engine = null
    }

    private fun buildEventHandler() = object : IRtcEngineEventHandler() {

        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            onJoinSuccess?.invoke()
        }

        override fun onUserJoined(uid: Int, elapsed: Int) {
            onRemoteUserJoined?.invoke(uid)
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            onRemoteUserLeft?.invoke()
        }

        override fun onError(err: Int) {
            onError?.invoke("Agora error code: $err")
        }
    }
}