package com.livestreaming.streamly.ui.setup.data.remote

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.livestreaming.streamly.config.utils.AppUtils
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.core.remote.Collections.STREAMS
import com.livestreaming.streamly.ui.setup.data.remote.dto.StartStreamRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class SetupRemoteDataSource @Inject constructor(@param:ApplicationContext private val context: Context) {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun startStream(request: StartStreamRequest): Stream? = try {
        val currentUser = AppUtils.getCurrentUser(context)
        val date = Calendar.getInstance().time
        val docRef = firestore.collection(STREAMS).document()
        val streamId = docRef.id

        val stream = Stream(
            id = streamId,
            hostId = currentUser?.id.toString(),
            hostName = currentUser?.name ?: "",
            agoraChannelId = "",
            title = request.title,
            isLive = false,
            viewerCount = 0,
            createdAt = date.time,
            hostPhotoUrl = currentUser?.avatar,
            description = request.desc,
            comments = null,
            viewers = null
        )

        docRef.set(stream).await()
        stream
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Starting stream failed")
    }
}