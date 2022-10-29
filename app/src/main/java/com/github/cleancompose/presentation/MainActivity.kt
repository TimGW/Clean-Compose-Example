package com.github.cleancompose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.cleancompose.presentation.repo.navigation.RepoDestination
import com.github.cleancompose.presentation.repo.navigation.RepoNavHost
import com.github.cleancompose.presentation.repo.navigation.screens
import com.github.cleancompose.presentation.theme.CleanComposeTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleanComposeTheme {
                CleanComposeApp()
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun CleanComposeApp() {
        val navController = rememberAnimatedNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = screens.find { currentDestination?.route?.contains(it.route) ?: true }
            ?: RepoDestination.ListScreen

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(currentScreen.pageTitle)) },
                    backgroundColor = MaterialTheme.colors.surface,
                    navigationIcon = navIcon(navController = navController)
                )
            }
        ) { innerPadding ->
            RepoNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    private fun navIcon(navController: NavController): @Composable (() -> Unit)? {
        return if (navController.previousBackStackEntry != null) {
            {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        } else {
            null
        }
    }
}
