package com.andrea.favoritedoggies.viewmodels

import com.andrea.favoritedoggies.data.DataStoreRepository
import com.andrea.favoritedoggies.data.DogRepository
import com.andrea.favoritedoggies.data.models.UserPreferences
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ImagesViewModelTests {

    @MockK
    private lateinit var imagesRepository: DogRepository
    @MockK
    private lateinit var dataStoreRepository: DataStoreRepository
    private lateinit var viewModel: ImagesViewModel
    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun testFlows_emptyFavorites() {
        coEvery { dataStoreRepository.getFavoriteBreeds() } returns flowOf(UserPreferences(emptySet()))
        viewModel = ImagesViewModel(imagesRepository, dataStoreRepository)
        runTest {
            val result = viewModel.userPreferencesFlow.first()
            Assert.assertEquals(ImagesViewModel.UserPreferencesUIState.Error, result)
        }
    }

    @Test
    fun testFlows_nonEmptyFavorites() {
        val favoriteBreeds = setOf("Golden Retriever", "Poodle/Miniature")
        val mockImagePaths = listOf("image1.jpg", "image2.jpg")

        coEvery { dataStoreRepository.getFavoriteBreeds() } returns flowOf(UserPreferences(favoriteBreeds))
        coEvery { imagesRepository.getBreedImagePath(any()) } returns mockImagePaths[0]
        coEvery { imagesRepository.getSubBreedImagePath(any(), any()) } returns mockImagePaths[1]

        runTest {
            viewModel = ImagesViewModel(imagesRepository, dataStoreRepository)
            val userPrefsResult = viewModel.userPreferencesFlow.first()
            val imagePathsResult = viewModel.imagePathUIState.value

            Assert.assertEquals(ImagesViewModel.UserPreferencesUIState.Success(favoriteBreeds), userPrefsResult)
            Assert.assertEquals(ImagesViewModel.ImageUIState.Success(mockImagePaths), imagePathsResult)
        }
    }

    @Test
    fun testGetNewImagePaths() {
        val favoriteBreeds = setOf("Golden Retriever", "Poodle/Miniature")
        val mockBreedImagePath = "breed_image.jpg"
        val mockSubBreedImagePath = "sub_breed_image.jpg"

        coEvery { dataStoreRepository.getFavoriteBreeds() } returns flowOf(UserPreferences(favoriteBreeds))
        coEvery { imagesRepository.getBreedImagePath("Golden Retriever") } returns mockBreedImagePath
        coEvery {
            imagesRepository.getSubBreedImagePath(
                "Poodle",
                "Miniature"
            )
        } returns mockSubBreedImagePath

        viewModel = ImagesViewModel(imagesRepository, dataStoreRepository)
        viewModel.getNewImagePaths(favoriteBreeds)

        runTest {
            Assert.assertEquals(
                ImagesViewModel.ImageUIState.Success(listOf(mockBreedImagePath, mockSubBreedImagePath)),
                viewModel.imagePathUIState.value
            )
        }
    }
}
