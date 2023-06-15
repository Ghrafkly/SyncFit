package com.example.syncfit.authentication

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.syncfit.R
import com.example.syncfit.database.entities.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(buildSignInRequest()).await()
        } catch (e: Exception) {
            Log.e("GoogleAuthClient", "Sign in failed")
            e.printStackTrace()
            if (e is CancellationException) throw e else null
        }

        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val idToken = credential.googleIdToken
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

        return try {
            val user = auth.signInWithCredential(firebaseCredential).await().user
            SignInResult(
                user = user?.run {
                    User(
                        email = email!!,
                        firstname = displayName,
                        phoneNumber = phoneNumber,
                        photoUrl = photoUrl?.toString(),
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            Log.e("GoogleAuthClient", "Sign in with intent failed")
            e.printStackTrace()
            if (e is CancellationException) throw e else null
            SignInResult(
                user = null,
                errorMessage = e.message
            )
        }

    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .setAutoSelectEnabled(true)
            .build()
    }

    fun getSignedInGoogleUser(): User? = auth.currentUser?.run {
        User(
            email = email!!,
            firstname = displayName,
            phoneNumber = phoneNumber,
            photoUrl = photoUrl?.toString(),
        )
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            Log.e("GoogleAuthClient", "Sign out failed")
            e.printStackTrace()
            if (e is CancellationException) throw e else null
        }
    }
}
