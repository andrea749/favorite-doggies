package com.andrea.favoritedoggies.data

import com.andrea.favoritedoggies.data.models.Breed
import kotlinx.coroutines.test.runTest

import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DogRepositoryUnitTests {
    @MockK
    private lateinit var dataSource: DogsApiService

    private lateinit var repository: DogRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = RemoteDogRepository(dataSource)
    }

    @Test
    fun getAllBreeds_successfulResponse() {
        val breedMap = mapOf(
            "Poodle" to listOf("Standard", "Miniature"),
            "Golden Retriever" to emptyList()
        )
        coEvery { dataSource.getAllBreeds() } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns mockk {
                every { status } returns "success"
                every { message } returns breedMap
            }
        }
        val expectedBreeds = listOf(
            Breed("Poodle", listOf("Standard", "Miniature")),
            Breed("Standard", parentBreed = "Poodle"),
            Breed("Miniature", parentBreed = "Poodle"),
            Breed("Golden Retriever")
        )
        runTest {
            val result = repository.getAllBreeds()
            Assert.assertEquals(expectedBreeds, result)
        }
    }

    @Test
    fun getAllBreeds_successfulResponseEmptyBody() {
        val breedMap = emptyMap<String, List<String>>()
        coEvery { dataSource.getAllBreeds() } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns mockk {
                every { status } returns "success"
                every { message } returns breedMap
            }
        }
        val expectedBreeds = emptyList<Breed>()
        runTest {
            val result = repository.getAllBreeds()
            Assert.assertEquals(expectedBreeds, result)
        }
    }

    @Test
    fun getBreedImagePath_successfulResponse() {
        val testBreed = "Poodle"
        val testImageUrl = "https://example.com/image.jpg"
        coEvery { dataSource.getBreedImagePath(testBreed) } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns mockk {
                every { status } returns "success"
                every { message } returns testImageUrl
            }
        }
        runTest {
            val result = repository.getBreedImagePath(testBreed)
            Assert.assertEquals(testImageUrl, result)
        }
    }
}