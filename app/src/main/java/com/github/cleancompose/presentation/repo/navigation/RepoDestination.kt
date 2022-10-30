package com.github.cleancompose.presentation.repo.navigation

import androidx.annotation.StringRes
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.github.cleancompose.R

sealed class RepoDestination(
    val route: String, @StringRes val pageTitle: Int
) {
    object ListScreen : RepoDestination("list", R.string.fragment_repo_list_label)
    object DetailsScreen : RepoDestination("details", R.string.fragment_repo_details_label) {
        const val repoArg = "repo"
        val routeWithArgs = "$route/{$repoArg}"
        val arguments = listOf(navArgument(repoArg) { type = NavType.StringType })
    }
}

val screens = listOf(RepoDestination.ListScreen, RepoDestination.DetailsScreen)
