package com.livestreaming.streamly.ui.broadcast.data.remote

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.model.StreamStatus
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.core.remote.Collections.STREAMS
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BroadcastRemoteDataSource @Inject constructor(@param:ApplicationContext private val context: Context) {
    private val firestore = FirebaseFirestore.getInstance()

    fun observeStream(streamId: String): Flow<Stream?> = callbackFlow {
        if (streamId.isBlank()) {
            close(IllegalArgumentException("streamId cannot be empty"))
            return@callbackFlow
        }

        val listener = firestore
            .collection(STREAMS)
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

    suspend fun startStream(stream: Stream): Stream = try {
        val docRef = firestore.collection(STREAMS).document()
        val streamId = docRef.id

        val updateStream = stream.copy(id = streamId)
        docRef.set(updateStream).await()

        updateStream
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Starting stream failed")
    }

    suspend fun endStream(stream: Stream): Void? = try {
        firestore
            .collection(STREAMS)
            .document(stream.id)
            .update(
                mapOf(
                    "status" to StreamStatus.Ended,
                    "endedAt" to stream.endedAt,
                    "status" to stream.status
                )
            )
            .await()
    } catch (e: Exception) {
        print(e)
        throw ApiException.UnknownException(context, e.message ?: "Starting stream failed")
    }

    suspend fun toggleMic(stream: Stream): Void? = try {
        firestore
            .collection(STREAMS)
            .document(stream.id)
            .update(mapOf("muted" to stream.muted))
            .await()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Toggle mic failed")
    }

    suspend fun toggleCamera(stream: Stream): Void? = try {
        firestore
            .collection(STREAMS)
            .document(stream.id)
            .update(mapOf("cameraOff" to stream.cameraOff))
            .await()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Toggle camera failed")
    }

    suspend fun toggleStatus(streamId: String, status: StreamStatus): Void? = try {
        firestore
            .collection(STREAMS)
            .document(streamId)
            .update(mapOf("status" to status.value))
            .await()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Toggle camera failed")
    }
}