package com.andrea.favoritedoggies.data

import android.util.Log
import com.andrea.favoritedoggies.data.models.Breed
import java.net.UnknownHostException
import javax.inject.Inject

class RemoteDogRepository @Inject constructor(private val apiService: DogsApiService): DogRepository {
    override suspend fun getAllBreeds(): List<Breed> {
        try {
            Log.d("RemoteDogRepository", "getAllBreeds()")
            val result = apiService.getAllBreeds()
            result.body()?.let {
                if (it.status == "success") {
                    return flattenBreedMap(it.message)
                }
            }
        } catch (_: UnknownHostException) {}
        return emptyList()
    }

    override suspend fun getBreedImagePath(breed: String): String {
        try {
            val result = apiService.getBreedImagePath(breed)
            result.body()?.let {
                if (it.status == "success") {
                    return it.message
                }
            }
        } catch (_: UnknownHostException) {}
        return ""
    }

    override suspend fun getSubBreedImagePath(breed: String, subBreed: String): String {
        try {
            val result = apiService.getSubBreedImagePath(breed, subBreed)
            result.body()?.let {
                if (it.status == "success") {
                    return it.message
                }
            }
        } catch (_: UnknownHostException) {}
        return ""
    }

    private fun flattenBreedMap(map: Map<String, List<String>>): List<Breed> {
        val result = mutableListOf<Breed>()
        map.map { entry ->
            result.add(Breed(breed = entry.key, subBreeds = entry.value))
            entry.value.map { subBreed ->
                result.add(Breed(breed = subBreed, parentBreed = entry.key))
            }
        }
        return result
    }
}