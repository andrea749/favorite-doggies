package com.andrea.favoritedoggies.data.models

import org.junit.Assert
import org.junit.Test

class BreedModelTests {

    @Test
    fun getAssocBreeds_empty() {
        val breed = Breed("")
        val expectedAssocBreeds = listOf("")
        val assocBreeds = breed.getAssocBreeds()
        Assert.assertEquals(expectedAssocBreeds, assocBreeds)
    }
    @Test
    fun getAssocBreeds_noSubBreeds() {
        val breed = Breed("Breed")
        val expectedAssocBreeds = listOf("Breed")
        val assocBreeds = breed.getAssocBreeds()
        Assert.assertEquals(expectedAssocBreeds, assocBreeds)
    }

    @Test
    fun getAssocBreeds_withSubBreeds() {
        val breed = Breed("Breed", listOf("SubBreed1", "SubBreed2"))
        val expectedAssocBreeds = listOf("Breed", "Breed/SubBreed1", "Breed/SubBreed2")
        val assocBreeds = breed.getAssocBreeds()
        Assert.assertEquals(expectedAssocBreeds, assocBreeds)
    }

    @Test
    fun getAssocBreeds_subBreedWithParent() {
        val breed = Breed("Breed", parentBreed = "Parent")
        val expectedAssocBreeds = listOf("Parent/Breed", "Parent")
        val assocBreeds = breed.getAssocBreeds(true)
        Assert.assertEquals(expectedAssocBreeds, assocBreeds)
    }

    @Test
    fun getAssocBreeds_subBreedNoParent() {
        val breed = Breed("Breed", parentBreed = "Parent")
        val expectedAssocBreeds = listOf("Parent/Breed")
        val assocBreeds = breed.getAssocBreeds(false)
        Assert.assertEquals(expectedAssocBreeds, assocBreeds)
    }

    @Test
    fun getAssocBreeds_withParentNoEffect() {
        val breed = Breed("Golden Retriever")
        val expectedAssocBreeds = listOf("Golden Retriever")
        val assocBreedsNoParent = breed.getAssocBreeds(false)
        val assocBreedsWithParent = breed.getAssocBreeds(true)
        Assert.assertEquals(expectedAssocBreeds, assocBreedsNoParent)
        Assert.assertEquals(expectedAssocBreeds, assocBreedsWithParent)
    }
}
