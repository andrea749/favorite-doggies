package com.andrea.favoritedoggies.viewmodels

import com.andrea.favoritedoggies.data.DataStoreRepository
import com.andrea.favoritedoggies.data.DogRepository
import com.andrea.favoritedoggies.data.models.Breed
import com.andrea.favoritedoggies.data.models.UserPreferences
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesPickerViewModelTests {

    @MockK
    private lateinit var imagesRepository: DogRepository

    @MockK
    private lateinit var dataStoreRepository: DataStoreRepository

    private lateinit var viewModel: FavoritesPickerViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun getBreeds_success() {
        val breeds = listOf(Breed("Golden Retriever"), Breed("Poodle"))
        coEvery { dataStoreRepository.getFavoriteBreeds() } returns flowOf(UserPreferences(emptySet()))
        coEvery { imagesRepository.getAllBreeds() } returns breeds
        viewModel = FavoritesPickerViewModel(imagesRepository, dataStoreRepository)

        runTest {
            viewModel.getBreeds()

            Assert.assertEquals(
                FavoritesPickerViewModel.BreedsUIState.Success(breeds),
                viewModel.breedsUIState.value
            )
        }
    }

    @Test
    fun getBreeds_empty() {
        val breeds = emptyList<Breed>()
        coEvery { dataStoreRepository.getFavoriteBreeds() } returns flowOf(UserPreferences(emptySet()))
        coEvery { imagesRepository.getAllBreeds() } returns breeds
        viewModel = FavoritesPickerViewModel(imagesRepository, dataStoreRepository)

        runTest {
            viewModel.getBreeds()

            Assert.assertEquals(
                FavoritesPickerViewModel.BreedsUIState.Error,
                viewModel.breedsUIState.value
            )
        }
    }

    @Test
    fun onFavoriteClick_favorite() {
        val breed = Breed("Golden Retriever")
        coEvery { imagesRepository.getAllBreeds() } returns emptyList()
        coEvery { dataStoreRepository.getFavoriteBreeds() } returns flowOf(UserPreferences(emptySet()))
        coEvery { dataStoreRepository.addFavorite(breed) } just runs
        val viewModel = FavoritesPickerViewModel(imagesRepository, dataStoreRepository)

        runTest {
            viewModel.onFavoriteClick(breed)

            coVerify { dataStoreRepository.addFavorite(breed) }
        }
    }

    @Test
    fun onFavoriteClick_unfavorite() {
        val breed = Breed("Golden Retriever", isFavorite = true)
        coEvery { imagesRepository.getAllBreeds() } returns emptyList()
        coEvery { dataStoreRepository.getFavoriteBreeds() } returns flowOf(UserPreferences(emptySet()))
        coEvery { dataStoreRepository.removeFavorite(breed) } just runs
        val viewModel = FavoritesPickerViewModel(imagesRepository, dataStoreRepository)

        runTest {
            viewModel.onFavoriteClick(breed)

            coVerify { dataStoreRepository.removeFavorite(breed) }
        }
    }
}