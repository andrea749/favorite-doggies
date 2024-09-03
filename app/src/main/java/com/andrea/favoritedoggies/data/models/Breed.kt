package com.andrea.favoritedoggies.data.models

data class Breed(
    val breed: String,
    val subBreeds: List<String> = emptyList(),
    val parentBreed: String = "",
    val isSubBreed: Boolean = parentBreed.isNotBlank(),
    val isFavorite: Boolean = false,
) {
    /* Helper function to get parent/subbreeds for saving to and removing from datastore.
    * In case of subbreeds, append parent breed name for uniqueness.
    * withParent = true is used if we are favoriting a subbreed and want to also favorite the
    * parent breed. There should be no effect if it is not a subbreed. */
    fun getAssocBreeds(withParent: Boolean = false): List<String> {
        val breedName = if (isSubBreed) "$parentBreed/$breed" else breed
        val result = mutableListOf(breedName) + subBreeds.map { subBreed -> "$breed/$subBreed" }
        return if (withParent && parentBreed.isNotBlank()) result + parentBreed else result
    }
}
