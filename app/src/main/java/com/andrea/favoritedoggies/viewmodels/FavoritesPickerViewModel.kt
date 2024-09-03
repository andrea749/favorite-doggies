package com.andrea.favoritedoggies.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrea.favoritedoggies.data.DataStoreRepository
import com.andrea.favoritedoggies.data.DogRepository
import com.andrea.favoritedoggies.data.models.Breed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesPickerViewModel @Inject constructor(
    private val imagesRepository: DogRepository,
    private val dataStoreRepository: DataStoreRepository,
): ViewModel() {
    private val userPreferencesFlow = dataStoreRepository.getFavoriteBreeds()
    private val _breedsUiState = MutableStateFlow<BreedsUIState>(BreedsUIState.Loading)
    val breedsUIState = _breedsUiState
        .combine(userPreferencesFlow) { breedsUiState, userPref ->
            if (breedsUiState is BreedsUIState.Success) {
                BreedsUIState.Success(breedsUiState.breeds.map { breed: Breed ->
                    breed.copy(isFavorite = breed.breed in userPref.favorites || "${breed.parentBreed}/${breed.breed}" in userPref.favorites)
                })
            } else breedsUiState
        }.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, BreedsUIState.Loading)

    init {
        getBreeds()
    }

    @VisibleForTesting
    fun getBreeds() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = imagesRepository.getAllBreeds()
                if (result.isEmpty()) {
                    _breedsUiState.value = BreedsUIState.Error
                } else {
                    _breedsUiState.value = BreedsUIState.Success(result)
                }
            }
        }
    }

    private fun favoriteBreed(breed: Breed) {
        viewModelScope.launch {
            dataStoreRepository.addFavorite(breed)
        }
    }

    private fun unfavoriteBreed(breed: Breed) {
        viewModelScope.launch {
            dataStoreRepository.removeFavorite(breed)
        }
    }
    
    fun onFavoriteClick(breed: Breed) {
        if (breed.isFavorite) unfavoriteBreed(breed) else favoriteBreed(breed)
    }

    sealed interface BreedsUIState {
        data class Success(val breeds: List<Breed>): BreedsUIState
        data object Error: BreedsUIState
        data object Loading: BreedsUIState
    }
}