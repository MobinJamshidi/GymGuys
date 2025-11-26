package com.example.gymguys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

data class WorkoutLoggerUiState(
    val categories: List<MenuItemCategory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaving: Boolean = false
)

class WorkoutLoggerViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    private val _uiState = MutableStateFlow(WorkoutLoggerUiState())
    val uiState: StateFlow<WorkoutLoggerUiState> = _uiState.asStateFlow()
    
    init {
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                loadWorkouts()
            } else {
                _uiState.value = _uiState.value.copy(
                    categories = sampleGymData,
                    isLoading = false
                )
            }
        }
        loadWorkouts()
    }
    

    fun loadWorkouts() {
        val user = auth.currentUser
        if (user == null) {
            _uiState.value = _uiState.value.copy(
                categories = sampleGymData,
                isLoading = false
            )
            return
        }
        
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                val snapshot = firestore
                    .collection("users")
                    .document(user.uid)
                    .collection("workouts")
                    .get()
                    .await()
                
                val loadedCategories = snapshot.documents.map { doc ->
                    val title = doc.getString("title") ?: ""
                    val itemsData = doc.get("items") as? List<Map<String, Any>> ?: emptyList()
                    
                    val items = itemsData.map { itemMap ->
                        ExerciseItem(
                            id = itemMap["id"] as? String ?: java.util.UUID.randomUUID().toString(),
                            name = itemMap["name"] as? String ?: "",
                            sets = itemMap["sets"] as? String ?: "",
                            reps = itemMap["reps"] as? String ?: "",
                            weightKg = itemMap["weightKg"] as? String ?: ""
                        )
                    }
                    
                    MenuItemCategory(
                        id = doc.id,
                        title = title,
                        items = items
                    )
                }
                
                val categories = loadedCategories
                
                _uiState.value = _uiState.value.copy(
                    categories = categories,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {

                val errorString = (e.message ?: e.toString()).lowercase()
                
                val errorMessage = when {
                    errorString.contains("403") ||
                    errorString.contains("forbidden") ||
                    errorString.contains("json conversion failed") ||
                    errorString.contains("doctype html") ||
                    errorString.contains("html") && errorString.contains("403") ||
                    errorString.contains("network") ||
                    errorString.contains("unable to resolve host") ||
                    errorString.contains("failed to connect") ||
                    errorString.contains("connection") ||
                    errorString.contains("timeout") ||
                    errorString.contains("no internet") ||
                    errorString.contains("socket") ||
                    errorString.contains("unreachable") ||
                    errorString.contains("identitytoolkit") && errorString.contains("403") ->
                        "you are not connected to the internet"
                    else -> "Failed to load workouts: ${e.message}"
                }
                _uiState.value = _uiState.value.copy(
                    categories = sampleGymData,
                    isLoading = false,
                    error = errorMessage
                )
            }
        }
    }
    

    fun saveWorkouts(categories: List<MenuItemCategory>) {
        val user = auth.currentUser
        if (user == null) {
            // User not logged in, can't save
            return
        }
        
        _uiState.value = _uiState.value.copy(isSaving = true, error = null)
        
        viewModelScope.launch {
            try {
                val workoutsRef = firestore
                    .collection("users")
                    .document(user.uid)
                    .collection("workouts")
                
                // Get existing workouts
                val existingSnapshot = workoutsRef.get().await()
                
                // Delete all existing workouts
                existingSnapshot.documents.forEach { doc ->
                    doc.reference.delete().await()
                }
                
                // Save new workouts
                categories.forEach { category ->
                    val workoutData = hashMapOf(
                        "title" to category.title,
                        "items" to category.items.map { item ->
                            hashMapOf(
                                "id" to item.id,
                                "name" to item.name,
                                "sets" to item.sets,
                                "reps" to item.reps,
                                "weightKg" to item.weightKg
                            )
                        },
                        "updatedAt" to com.google.firebase.Timestamp.now()
                    )
                    
                    workoutsRef.document(category.id).set(workoutData).await()
                }
                
                _uiState.value = _uiState.value.copy(
                    categories = categories,
                    isSaving = false,
                    error = null
                )
            } catch (e: Exception) {
                // Convert exception to string to check for HTML responses
                val errorString = (e.message ?: e.toString()).lowercase()
                
                val errorMessage = when {
                    // Check for internet/network errors first (including 403 Forbidden from Google)
                    errorString.contains("403") ||
                    errorString.contains("forbidden") ||
                    errorString.contains("json conversion failed") ||
                    errorString.contains("doctype html") ||
                    errorString.contains("html") && errorString.contains("403") ||
                    errorString.contains("network") ||
                    errorString.contains("unable to resolve host") ||
                    errorString.contains("failed to connect") ||
                    errorString.contains("connection") ||
                    errorString.contains("timeout") ||
                    errorString.contains("no internet") ||
                    errorString.contains("socket") ||
                    errorString.contains("unreachable") ||
                    errorString.contains("identitytoolkit") && errorString.contains("403") ->
                        "به اینترنت متصل نیستید"
                    else -> "Failed to save workouts: ${e.message}"
                }
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = errorMessage
                )
            }
        }
    }
    
    /**
     * Update categories list (called when user makes changes)
     */
    fun updateCategories(categories: List<MenuItemCategory>) {
        _uiState.value = _uiState.value.copy(categories = categories)
        // Auto-save when categories are updated
        saveWorkouts(categories)
    }
    
    /**
     * Add a new category
     */
    fun addCategory(title: String) {
        val newCategory = MenuItemCategory(
            title = title,
            items = emptyList()
        )
        val updatedCategories = _uiState.value.categories + newCategory
        updateCategories(updatedCategories)
    }
    
    /**
     * Update a category
     */
    fun updateCategory(updatedCategory: MenuItemCategory) {
        val categories = _uiState.value.categories.toMutableList()
        val index = categories.indexOfFirst { it.id == updatedCategory.id }
        if (index != -1) {
            categories[index] = updatedCategory
            updateCategories(categories)
        }
    }
    
    /**
     * Delete a category
     */
    fun deleteCategory(categoryId: String) {
        val user = auth.currentUser
        if (user != null) {
            viewModelScope.launch {
                try {
                    firestore
                        .collection("users")
                        .document(user.uid)
                        .collection("workouts")
                        .document(categoryId)
                        .delete()
                        .await()
                } catch (e: Exception) {
                    // Ignore delete errors
                }
            }
        }
        
        val categories = _uiState.value.categories.filter { it.id != categoryId }
        updateCategories(categories)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

