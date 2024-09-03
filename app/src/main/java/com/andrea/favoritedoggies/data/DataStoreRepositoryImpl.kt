package com.andrea.favoritedoggies.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.andrea.favoritedoggies.data.models.Breed
import com.andrea.favoritedoggies.data.models.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataStoreRepositoryImpl @Inject constructor (private val dataStore: DataStore<Preferences>): DataStoreRepository {

    override fun getFavoriteBreeds(): Flow<UserPreferences> {
        return dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val favorites = preferences[FAVORITES]?: emptySet()
                UserPreferences(favorites)
            }
    }

    override suspend fun addFavorite(
        breed: Breed,
    ) {
        dataStore.edit { preferences ->
            val favorites: MutableSet<String>? = preferences[FAVORITES]?.toMutableSet()
            favorites?.addAll(breed.getAssocBreeds(false))
            preferences[FAVORITES] = favorites?.toSet() ?: emptySet()
        }
    }

    override suspend fun removeFavorite(
        breed: Breed,
    ) {
        dataStore.edit { preferences ->
            val favorites: MutableSet<String>? = preferences[FAVORITES]?.toMutableSet()
            breed.getAssocBreeds(true).map { breed -> favorites?.remove(breed) }
            preferences[FAVORITES] = favorites?.toSet() ?: emptySet()
        }
    }

    private companion object PreferencesKeys {
        val FAVORITES = stringSetPreferencesKey("favorites")
    }
}