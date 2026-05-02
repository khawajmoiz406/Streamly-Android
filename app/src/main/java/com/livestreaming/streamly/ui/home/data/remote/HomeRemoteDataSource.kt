package com.livestreaming.streamly.ui.home.data.remote

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

class HomeRemoteDataSource @Inject constructor(@param:ApplicationContext private val context: Context) {
    private val firestore = FirebaseFirestore.getInstance()

    fun observeLiveStreams(): Flow<List<Stream>?> = callbackFlow {
        val listener = firestore
            .collection(STREAMS)
            .whereEqualTo("status", StreamStatus.Live.value)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val streams = snapshot?.documents?.mapNotNull {
                    it.toObject(Stream::class.java)
                }

                trySend(streams)
            }
        awaitClose { listener.remove() }
    }

    suspend fun getLiveStreams(): List<Stream>? = try {
        val docRef = firestore
            .collection(STREAMS)
            .whereEqualTo("status", StreamStatus.Live.value)
            .get()
            .await()

        docRef.documents.mapNotNull { it.toObject(Stream::class.java) }
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Getting live streams failed")
    }
}