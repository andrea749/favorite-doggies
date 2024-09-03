package com.andrea.favoritedoggies.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.andrea.favoritedoggies.ui.screens.FavoritesPickerNavItem
import com.andrea.favoritedoggies.ui.screens.FavoritesPickerScreen
import com.andrea.favoritedoggies.ui.screens.ImagesNavItem
import com.andrea.favoritedoggies.ui.screens.ImagesScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    ) {
    NavHost(navController, startDestination = FavoritesPickerNavItem.route) {
        composable(ImagesNavItem.route) {
            ImagesScreen(navToFavoritesPickerScreen = { navController.navigate(FavoritesPickerNavItem.route) }, modifier = modifier)
        }
        composable(FavoritesPickerNavItem.route) {
            FavoritesPickerScreen(navToImagesScreen = { navController.navigate(ImagesNavItem.route) }, modifier = modifier)
        }
    }
}