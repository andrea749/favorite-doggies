package com.andrea.favoritedoggies.data

import com.andrea.favoritedoggies.data.models.Breeds
import com.andrea.favoritedoggies.data.models.ImagePath
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DogsApiService {
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): Response<Breeds>

    @GET("breed/{breed}/images/random")
    suspend fun getBreedImagePath(@Path("breed") breed: String): Response<ImagePath>

    @GET("breed/{breed}/{subBreed}/images/random")
    suspend fun getSubBreedImagePath(
        @Path("breed") breed: String,
        @Path("subBreed") subBreed: String
    ): Response<ImagePath>

}