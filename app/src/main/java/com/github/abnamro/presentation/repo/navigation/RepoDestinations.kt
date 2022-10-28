package com.github.abnamro.presentation.repo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface RepoDestination {
    val icon: ImageVector
    val route: String
}

object Overview : RepoDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "overview"
}

object Details : RepoDestination {
    // Added for simplicity, this icon will not in fact be used, as SingleAccount isn't
    // part of the RallyTabRow selection
    override val icon = Icons.Filled.Money
    override val route = "details"
    const val accountTypeArg = "account_type"
    val routeWithArgs = "$route/{$accountTypeArg}"
    val arguments = listOf(
        navArgument(accountTypeArg) { type = NavType.StringType }
    )
}

val screens = listOf(Overview, Details)
