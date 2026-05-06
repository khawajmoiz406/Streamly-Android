package com.livestreaming.streamly.ui.profile.data.remote

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.livestreaming.streamly.core.model.ProfileData
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.model.StreamStatus
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.core.remote.Collections.STREAMS
import com.livestreaming.streamly.core.remote.Collections.VIEWERS
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    suspend fun getProfileData(userId: String): ProfileData = try {
        val streamsSnap = firestore.collection(STREAMS)
            .whereEqualTo("hostId", userId)
            .orderBy("startedAt", Query.Direction.DESCENDING)
            .get()
            .await()

        val streams = streamsSnap.documents.mapNotNull { it.toObject(Stream::class.java) }
        buildProfileData(streams)
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Loading profile failed")
    }

    fun observeProfileData(userId: String): Flow<ProfileData> = callbackFlow {
        val listener = firestore.collection(STREAMS)
            .whereEqualTo("hostId", userId)
            .orderBy("startedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val streams = snapshot?.documents?.mapNotNull {
                    it.toObject(Stream::class.java)
                } ?: emptyList()

                launch {
                    runCatching { buildProfileData(streams) }
                        .onSuccess { trySend(it) }
                        .onFailure { close(it) }
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun signOut() = try {
        firebaseAuth.signOut()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Sign out failed")
    }

    private suspend fun buildProfileData(streams: List<Stream>): ProfileData {
        val viewerCounts = coroutineScope {
            streams.map { stream ->
                async {
                    firestore.collection(STREAMS)
                        .document(stream.id)
                        .collection(VIEWERS)
                        .get()
                        .await()
                        .size()
                }
            }.awaitAll()
        }

        val ended = streams.filter { it.status == StreamStatus.Ended.value }
        val totalTime = ended.sumOf { it.endedAt - it.startedAt }
        val avg = if (streams.isEmpty()) 0 else viewerCounts.sum() / streams.size

        return ProfileData(
            streams = streams,
            streamCount = ended.size,
            totalStreamTimeMs = totalTime,
            avgViewers = avg,
        )
    }
}