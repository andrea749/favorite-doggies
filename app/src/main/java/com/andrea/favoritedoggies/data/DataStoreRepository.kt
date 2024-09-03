package com.andrea.favoritedoggies.data

import com.andrea.favoritedoggies.data.models.Breed
import com.andrea.favoritedoggies.data.models.UserPreferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    fun getFavoriteBreeds(): Flow<UserPreferences>

    suspend fun addFavorite(breed: Breed)

    suspend fun removeFavorite(breed: Breed)

}

