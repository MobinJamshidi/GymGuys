package com.example.gymguys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: FirebaseUser? = null,
    val isSignedIn: Boolean = false
)

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        auth.currentUser?.let { currentUser: FirebaseUser ->
            _uiState.value = _uiState.value.copy(
                user = currentUser,
                isSignedIn = true
            )
        }
    }

    fun signInWithGoogle(idToken: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                val user = result.user

                if (user != null) {
                    val userDoc = firestore.collection("users").document(user.uid).get().await()

                    if (!userDoc.exists()) {
                        val userData = hashMapOf(
                            "fullName" to (user.displayName ?: "No Name"),
                            "email" to (user.email ?: ""),
                            "createdAt" to com.google.firebase.Timestamp.now()
                        )
                        firestore.collection("users").document(user.uid).set(userData).await()
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = user,
                        isSignedIn = true,
                        error = null
                    )
                    onSuccess()
                } else {
                    onError("Google Sign In failed: User is null")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Google Sign In Failed"
                _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                onError(errorMessage)
            }
        }
    }
    // -------------------------------------

    fun signUp(
        email: String,
        password: String,
        fullName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank() || fullName.isBlank()) {
            onError("Please fill in all fields")
            return
        }

        if (password.length < 6) {
            onError("Password must be at least 6 characters")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onError("Please enter a valid email address")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val result: AuthResult = auth.createUserWithEmailAndPassword(email, password).await()

                val userData = hashMapOf(
                    "fullName" to fullName,
                    "email" to email,
                    "createdAt" to com.google.firebase.Timestamp.now()
                )

                val user: FirebaseUser? = result.user
                if (user != null) {
                    firestore.collection("users")
                        .document(user.uid)
                        .set(userData)
                        .await()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = user,
                        isSignedIn = true,
                        error = null
                    )

                    onSuccess()
                } else {
                    onError("Failed to create user account")
                }
            } catch (e: Exception) {
                val errorString = (e.message ?: e.toString()).lowercase()
                val errorMessage = getFriendlyErrorMessage(errorString, e)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
                onError(errorMessage)
            }
        }
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onError("Please fill in all fields")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onError("Please enter a valid email address")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val result: AuthResult = auth.signInWithEmailAndPassword(email, password).await()

                val user: FirebaseUser? = result.user
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = user,
                    isSignedIn = true,
                    error = null
                )

                onSuccess()
            } catch (e: Exception) {
                val errorString = (e.message ?: e.toString()).lowercase()
                val errorMessage = getFriendlyErrorMessage(errorString, e)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
                onError(errorMessage)
            }
        }
    }

    private fun getFriendlyErrorMessage(errorString: String, e: Exception): String {
        return when {
            errorString.contains("403") || errorString.contains("network") || errorString.contains("no internet") ->
                "You are not connected to the internet"
            e.message?.contains("email-already-in-use", ignoreCase = true) == true ->
                "This email is already registered."
            e.message?.contains("user-not-found", ignoreCase = true) == true ->
                "No account found with this email."
            e.message?.contains("wrong-password", ignoreCase = true) == true ->
                "Incorrect password."
            else -> e.message ?: "An error occurred."
        }
    }

    fun signOut() {
        auth.signOut()
        _uiState.value = AuthUiState()
    }
}