package com.github.abnamro.presentation.repo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.abnamro.presentation.repo.list.RepoList

@Composable
fun RepoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Overview.route,
        modifier = modifier
    ) {
        composable(route = Overview.route) {
            RepoList(
                onRepoClick = {
                    navController.navigateToDetails(it)
                }
            )
        }
        composable(
            route = Details.route,
            arguments = Details.arguments,
        ) {
//            val accountType = navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)
//            SingleAccountScreen(accountType)
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

private fun NavHostController.navigateToDetails(accountType: String) {
    this.navigateSingleTopTo("${Details.route}/$accountType")
}
