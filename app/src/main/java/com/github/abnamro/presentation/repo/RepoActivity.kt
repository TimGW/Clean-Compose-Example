package com.github.abnamro.presentation.repo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.rememberNavController
import com.github.abnamro.presentation.repo.navigation.Destination
import com.github.abnamro.presentation.repo.navigation.RepoNavHost
import com.github.abnamro.presentation.repo.navigation.screens
import com.github.abnamro.presentation.theme.ABNAMROAssessmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ABNAMROAssessmentTheme {
                AbnAmroApp()
            }
        }
    }

    @Composable
    fun AbnAmroApp() {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = screens.find { currentDestination?.route?.contains(it.route) ?: true }
            ?: Destination.List

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
