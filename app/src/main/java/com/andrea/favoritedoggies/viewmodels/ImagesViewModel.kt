package com.andrea.favoritedoggies.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrea.favoritedoggies.data.DataStoreRepository
import com.andrea.favoritedoggies.data.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    dataStoreRepository: DataStoreRepository,
    ): ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>()
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val userPreferencesFlow = dataStoreRepository
        .getFavoriteBreeds()
        .combine(refreshTrigger) {
            userPreferences, _ -> userPreferences
        }.map { userPreferences ->
            if (userPreferences.favorites.isEmpty()) {
                UserPreferencesUIState.Error
            } else {
//                getNewImagePaths(userPreferences.favorites)
                UserPreferencesUIState.Success(userPreferences.favorites)
                getNewImagePaths()
            }
        }.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, UserPreferencesUIState.Loading)

    private val _imagePathUIState = MutableStateFlow<ImageUIState>(ImageUIState.Loading)
    val imagePathUIState = _imagePathUIState.asStateFlow()
    private var batchStart = 0 // index of the first image to download
    private val batchSize = 3

//    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNewImagePaths() {
//        fun getNewImagePaths(favoriteBreeds: Set<String>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favoriteBreeds = (userPreferencesFlow.value as? UserPreferencesUIState.Success)?.favorites?.toList()
                favoriteBreeds?.let { favorites ->
                    val imagePaths = mutableListOf<String>()
                    for (i in batchStart until min(batchStart + batchSize, favorites.size)) {
                        val isSubBreed = favorites[i].contains('/')
                        if (isSubBreed) {
                            val (breed, subBreed) = favorites[i].split('/')
                            imagePaths.add(dogRepository.getSubBreedImagePath(breed, subBreed))
                        } else {
                            imagePaths.add(dogRepository.getBreedImagePath(favorites[i]))
                        }
                    }
                    val imgList =
                        ((_imagePathUIState.value as? ImageUIState.Success)?.paths?.plus(imagePaths))
                            ?: imagePaths
                    _imagePathUIState.value = ImageUIState.Success(imgList)
                    batchStart = min(batchStart + batchSize, favorites.size)
                }
            }
        }
    }

    fun refreshPaths() {
        viewModelScope.launch {
            refreshTrigger.emit(Unit)
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
