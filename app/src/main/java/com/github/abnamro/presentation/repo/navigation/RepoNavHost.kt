package com.github.abnamro.presentation.repo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.abnamro.presentation.repo.details.RepoDetailsScreen
import com.github.abnamro.presentation.repo.list.RepoList
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun RepoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destination.List.route,
        modifier = modifier
    ) {
        composable(
            route = Destination.List.route
        ) {
            RepoList(onRepoClick = { fullName ->
                val encoded = URLEncoder.encode(fullName, StandardCharsets.UTF_8.toString())
                navController.navigate(route = "${Destination.Details.route}/$encoded")
            })
        }
        composable(
            route = Destination.Details.routeWithArgs,
            arguments = Destination.Details.arguments
        ) {
            RepoDetailsScreen()
        }
    }
}
