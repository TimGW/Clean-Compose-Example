package com.github.abnamro.presentation.repo.navigation

import androidx.annotation.StringRes
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.github.abnamro.R

sealed class Destination(
    val route: String, @StringRes val pageTitle: Int
) {
    object ListScreen : Destination("list", R.string.fragment_repo_list_label)
    object DetailsScreen : Destination("details", R.string.fragment_repo_details_label) {
        private const val queryArg = "query"
        val routeWithArgs = "$route/{$queryArg}"
        val arguments = listOf(navArgument(queryArg) { type = NavType.StringType })
    }
}

val screens = listOf(Destination.ListScreen, Destination.DetailsScreen)
