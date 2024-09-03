package com.andrea.favoritedoggies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andrea.favoritedoggies.ui.navigation.AppNavGraph
import com.andrea.favoritedoggies.ui.theme.FavoriteDoggiesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            FavoriteDoggiesTheme {
                MainScreen(
                    navController = navController,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    ) {
    Scaffold(modifier = modifier.fillMaxSize())
    { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        )
    }
}
