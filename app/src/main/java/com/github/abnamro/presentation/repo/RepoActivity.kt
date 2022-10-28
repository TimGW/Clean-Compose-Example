package com.github.abnamro.presentation.repo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.abnamro.presentation.repo.navigation.Overview
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
        val currentScreen = screens.find { it.route == currentDestination?.route } ?: Overview

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = currentScreen.route) },
                    backgroundColor = MaterialTheme.colors.surface,
                )
            }
        ) { innerPadding ->
            RepoNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
