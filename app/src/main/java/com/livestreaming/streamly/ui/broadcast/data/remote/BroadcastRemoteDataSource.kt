package com.livestreaming.streamly.ui.broadcast.data.remote

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.core.remote.Collections
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BroadcastRemoteDataSource @Inject constructor(@param:ApplicationContext private val context: Context) {
    private val firestore = FirebaseFirestore.getInstance()

    fun observeStream(streamId: String): Flow<Stream?> = callbackFlow {
        val listener = firestore
            .collection(Collections.STREAMS)
            .document(streamId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject(Stream::class.java))
            }
        awaitClose { listener.remove() }
    }

    suspend fun goLive(stream: Stream): Void = try {
        firestore
            .document(stream.id)
            .update(
                mapOf(
                    "isLive" to stream.isLive,
                    "agoraChannelId" to stream.agoraChannelId,
                    "startedAt" to stream.startedAt,
                )
            )
            .await()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Starting stream failed")
    }

    suspend fun endStream(stream: Stream): Void = try {
        firestore
            .document(stream.id)
            .update(
                mapOf(
                    "isLive" to stream.isLive,
                    "endedAt" to stream.endedAt,
                )
            )
            .await()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Starting stream failed")
    }

    suspend fun toggleMic(stream: Stream): Void = try {
        firestore
            .document(stream.id)
            .update(mapOf("isMute" to stream.isMuted))
            .await()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Toggle mic failed")
    }

    suspend fun toggleCamera(stream: Stream): Void = try {
        firestore
            .document(stream.id)
            .update(mapOf("isCameraOff" to stream.isCameraOff))
            .await()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Toggle camera failed")
    }

    suspend fun toggleStatus(stream: Stream): Void = try {
        firestore
            .document(stream.id)
            .update(mapOf("status" to stream.status))
            .await()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Toggle camera failed")
    }
}