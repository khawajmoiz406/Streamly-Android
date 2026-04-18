package com.livestreaming.streamly.ui.auth.data.remote

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.livestream.streamly.R
import com.livestreaming.streamly.core.model.AccountType
import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.ui.auth.data.mapper.toUser
import com.livestreaming.streamly.ui.auth.data.remote.dto.LoginRequest
import com.livestreaming.streamly.ui.auth.data.remote.dto.RegisterRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRemoteDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var credentialManager = CredentialManager.create(context)

    suspend fun login(request: LoginRequest): User? = try {
        if (request.accountType == AccountType.Email) {
            firebaseAuth.signInWithEmailAndPassword(
                request.email ?: "",
                request.password ?: ""
            ).await()
        } else {
            val authCred = getGoogleAuthCredentials()
            firebaseAuth.signInWithCredential(authCred)
                .await()
        }

        val type = request.accountType
        firebaseAuth.currentUser?.toUser(type)
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Login failed")
    }

    suspend fun register(request: RegisterRequest): User? = try {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(
            request.email ?: "",
            request.password ?: ""
        ).await()

        val photoUrl = request.profilePicture?.let { uploadPhoto(it) }

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(request.name)
            .setPhotoUri(photoUrl?.toUri())
            .build()

        authResult.user?.updateProfile(profileUpdates)?.await()

        firebaseAuth.currentUser?.toUser(AccountType.Email)
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Register failed")
    }

    suspend fun sendResetPasswordLink(email: String): Void? = try {
        firebaseAuth.sendPasswordResetEmail(email).await()
    } catch (e: Exception) {
        throw ApiException.UnknownException(context, e.message ?: "Login failed")
    }

    private suspend fun getGoogleAuthCredentials(): AuthCredential {
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            serverClientId = context.getString(R.string.default_web_client_id)
        ).build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val result = credentialManager.getCredential(context, request)

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
        return GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
    }

    private suspend fun uploadPhoto(uri: Uri): String = suspendCoroutine {
        MediaManager.get()
            .upload(uri)
            .option("folder", "profile_photos")
            .callback(object : UploadCallback {
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as String
                    it.resume(url)
                }

                override fun onError(requestId: String?, error: ErrorInfo) {
                    it.resumeWithException(
                        ApiException.UnknownException(
                            context = context,
                            exception = error.description
                        )
                    )
                }

                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            })
            .dispatch()
    }
}