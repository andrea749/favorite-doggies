package com.andrea.favoritedoggies.data

import com.andrea.favoritedoggies.data.models.Breed

interface DogRepository {
    suspend fun getAllBreeds(): List<Breed>

    suspend fun getBreedImagePath(breed: String): String

    suspend fun getSubBreedImagePath(
        breed: String,
        subBreed: String
    ): String

}