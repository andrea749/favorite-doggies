package com.andrea.favoritedoggies.data.models

/* For parsing server response to getBreedImagePath or getSubBreedImagePath */
data class ImagePath(
    val message: String,
    val status: String
)