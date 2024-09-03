package com.andrea.favoritedoggies.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrea.favoritedoggies.data.DataStoreRepository
import com.andrea.favoritedoggies.data.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    dataStoreRepository: DataStoreRepository,
    ): ViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val userPreferencesFlow = dataStoreRepository
        .getFavoriteBreeds()
        .map { userPreferences ->
            if (userPreferences.favorites.isEmpty()) {
                UserPreferencesUIState.Error
            } else {
                getNewImagePaths(userPreferences.favorites)
                UserPreferencesUIState.Success(userPreferences.favorites)
            }
        }.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, UserPreferencesUIState.Loading)
    private val _imagePathUIState = MutableStateFlow<ImageUIState>(ImageUIState.Loading)
    val imagePathUIState = _imagePathUIState.asStateFlow()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNewImagePaths(favoriteBreeds: Set<String>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val imagePaths = mutableListOf<String>()
                favoriteBreeds.forEach { entry ->
                    val isSubBreed = entry.contains('/')
                    if (isSubBreed) {
                        val (breed, subBreed) = entry.split('/')
                        imagePaths.add(dogRepository.getSubBreedImagePath(breed, subBreed))
                    } else {
                        imagePaths.add(dogRepository.getBreedImagePath(entry))
                    }
                }
                _imagePathUIState.value = ImageUIState.Success(imagePaths)
            }
        }
    }

    sealed interface ImageUIState {
        data class Success(val paths: List<String>): ImageUIState
        data object Error: ImageUIState
        data object Loading: ImageUIState
    }

    sealed interface UserPreferencesUIState {
        data class Success(val favorites: Set<String>): UserPreferencesUIState
        data object Error: UserPreferencesUIState
        data object Loading: UserPreferencesUIState
    }
}
