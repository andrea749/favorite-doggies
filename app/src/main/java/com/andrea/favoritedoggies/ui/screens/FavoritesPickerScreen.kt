package com.andrea.favoritedoggies.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andrea.favoritedoggies.R
import com.andrea.favoritedoggies.data.models.Breed
import com.andrea.favoritedoggies.ui.navigation.NavItem
import com.andrea.favoritedoggies.viewmodels.FavoritesPickerViewModel

object FavoritesPickerNavItem : NavItem {
    override val route = "favorites_picker"
}
@Composable
fun FavoritesPickerScreen(
    navToImagesScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesPickerViewModel = hiltViewModel(),
) {
    // using AppNavGraph provided function triggers a refresh of the images screen
    BackHandler {
        navToImagesScreen()
    }

    val breedData by viewModel.breedsUIState.collectAsState()
    val breedList = (breedData as? FavoritesPickerViewModel.BreedsUIState.Success)?.breeds ?: emptyList()
    Column(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        Title(text = "Favorites")
        AllBreeds(
            modifier = Modifier.weight(1f),
            breeds = breedList,
            onFavoriteClick = { breed -> viewModel.onFavoriteClick(breed) }
        )
        Button(
            onClick = navToImagesScreen,
            colors = ButtonDefaults.buttonColors(),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 30.dp, vertical = 10.dp),
            ) {
            Text(text = "Go to Images")
        }
    }
}

@Composable
fun Title(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            fontSize = 60.sp,
        )
    }
}

@Composable
fun AllBreeds(
    modifier: Modifier = Modifier,
    breeds: List<Breed> = emptyList(),
    onFavoriteClick: (Breed) -> Unit = {},
    ) {
    LazyColumn(modifier = modifier) {
        items(breeds) { breed ->
            BreedItem(
                breed = breed,
                onFavoriteClick = onFavoriteClick,
                )
        }
    }
}

@Composable
fun BreedItem(
    breed: Breed,
    modifier: Modifier = Modifier,
    onFavoriteClick: (Breed) -> Unit = {},
    ) {
    Column(modifier = modifier
        .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = if (breed.isSubBreed) 50.dp else 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.favorite_24px),
                tint = if (breed.isFavorite) Color.Red else Color.Gray,
                contentDescription = "favorite_24px",
                modifier = Modifier
                    .weight(0.2f)
                    .clickable { onFavoriteClick(breed) }
            )
            Column(
                modifier = Modifier.weight(0.8f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = breed.breed, fontSize = 20.sp)
            }
        }
        HorizontalDivider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp))
    }
}
