package com.andrea.favoritedoggies.data.models

// For parsing JSON
data class Breeds(
    val message: Map<String, List<String>>,
    val status: String,
)