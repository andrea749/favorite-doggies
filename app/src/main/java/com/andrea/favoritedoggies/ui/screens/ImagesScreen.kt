package com.andrea.favoritedoggies.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.andrea.favoritedoggies.R
import com.andrea.favoritedoggies.ui.navigation.NavItem
import com.andrea.favoritedoggies.viewmodels.ImagesViewModel


object ImagesNavItem : NavItem {
    override val route = "images"
}

@Composable
fun ImagesScreen(
    navToFavoritesPickerScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImagesViewModel = hiltViewModel(),
    ) {
    val imageUiState by viewModel.imagePathUIState.collectAsState(initial = ImagesViewModel.ImageUIState.Loading)
    val imgUrls = (imageUiState as? ImagesViewModel.ImageUIState.Success)?.paths ?: emptyList()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        CarouselTitle(text = "Dog Images")
        if (imgUrls.isEmpty()) {
            Column(
                modifier = Modifier.weight(0.8f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Add some favorites")
            }
        } else {
            ImageCarousel(
                imgUrls = imgUrls,
                modifier = Modifier.weight(0.8f),
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                ,
            onClick = navToFavoritesPickerScreen,
            ) {
            Text(text = "Add Favorites")
        }
    }
}

@Composable
fun CarouselTitle(text: String, modifier: Modifier = Modifier) {
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
fun ImageCarousel(
    imgUrls: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            modifier = modifier
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(imgUrls) {
                ImageCard(imgUrl = it)
            }
        }
    }

}

@Composable
fun ImageCard(
    imgUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imgUrl,
        contentDescription = "",
        modifier = modifier
            .wrapContentSize()
            .padding(vertical = 8.dp),
        contentScale = ContentScale.Fit,
        placeholder = painterResource(
            id = R.drawable.progress_activity_24dp
    ))
}