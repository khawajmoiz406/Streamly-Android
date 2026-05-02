package com.livestreaming.streamly.ui.watch.data.remote

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.core.model.Viewer
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.core.remote.Collections.COMMENTS
import com.livestreaming.streamly.core.remote.Collections.STREAMS
import com.livestreaming.streamly.core.remote.Collections.VIEWERS
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class WatchRemoteDataSource @Inject constructor(@param:ApplicationContext private val context: Context) {
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

    fun observeStreamComments(streamId: String): Flow<List<Comment>> = callbackFlow {
        if (streamId.isBlank()) {
            close(IllegalArgumentException("streamId cannot be empty"))
            return@callbackFlow
        }

        val listener = firestore
            .collection(STREAMS)
            .document(streamId)
            .collection(COMMENTS)
            .orderBy("sentAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val comments = snapshot?.documents?.mapNotNull {
                    it.toObject(Comment::class.java)
                } ?: emptyList()

                trySend(comments)
            }
        awaitClose { listener.remove() }
    }

    suspend fun addCommentToStream(streamId: String, comment: Comment): Comment = try {
        val docRef = firestore
            .collection(STREAMS)
            .document(streamId)
            .collection(COMMENTS)
            .document()

        val commentId = docRef.id
        val updateComment = comment.copy(id = commentId)
        docRef.set(updateComment).await()

        updateComment
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Starting stream failed")
    }

    suspend fun getStream(streamId: String): Stream? = try {
        val docRef = firestore
            .collection(STREAMS)
            .document(streamId)
            .get()
            .await()

        docRef.toObject(Stream::class.java)
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Starting stream failed")
    }

    suspend fun userJoinedStream(stream: Stream, user: User): Void? = try {
        val time = Calendar.getInstance().timeInMillis
        val viewer = Viewer(
            userId = user.id ?: "",
            userName = user.name ?: "",
            active = true,
            photoUrl = user.avatar,
            joinedAt = time,
            lastActiveAt = time,
        )

        val streamRef = firestore
            .collection(STREAMS)
            .document(stream.id)

        val viewerRef = firestore
            .collection(STREAMS)
            .document(stream.id)
            .collection(VIEWERS)
            .document(user.id ?: "")

        val batch = firestore.batch()
        batch.update(streamRef, "viewerCount", stream.viewerCount)
        batch.set(viewerRef, viewer)

        batch.commit().await()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Toggle mic failed")
    }

    suspend fun leaveStream(stream: Stream, user: User): Void? = try {
        val streamRef = firestore
            .collection(STREAMS)
            .document(stream.id)

        val viewerRef = firestore
            .collection(STREAMS)
            .document(stream.id)
            .collection(VIEWERS)
            .document(user.id ?: "")

        val batch = firestore.batch()
        batch.update(streamRef, "viewerCount", stream.viewerCount)
        batch.update(viewerRef, "viewer", "active", false)

        batch.commit().await()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Toggle mic failed")
    }
}