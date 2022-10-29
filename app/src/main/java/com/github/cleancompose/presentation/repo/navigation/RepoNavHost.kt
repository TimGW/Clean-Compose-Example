package com.github.cleancompose.presentation.repo.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.cleancompose.presentation.repo.details.RepoDetailsScreen
import com.github.cleancompose.presentation.repo.list.RepoList
import com.google.accompanist.navigation.animation.AnimatedNavHost
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.google.accompanist.navigation.animation.composable as animatedComposable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RepoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Destination.ListScreen.route,
        modifier = modifier
    ) {
        val shortAnimTime = 300
        val mediumAnimTime = 600
        val enterAnim = slideInVertically(
            animationSpec = tween(mediumAnimTime),
            initialOffsetY = { it / 6 }
        ).plus(fadeIn(animationSpec = tween(mediumAnimTime)))
        val exitAnim = slideOutVertically(
            animationSpec = tween(shortAnimTime),
            targetOffsetY = { it / 6 }
        ).plus(fadeOut(animationSpec = tween(shortAnimTime)))

        animatedComposable(
            route = Destination.ListScreen.route,
            enterTransition = { enterAnim },
            exitTransition = { exitAnim }
        ) {
            RepoList(onRepoClick = { fullName ->
                val encoded = URLEncoder.encode(fullName, StandardCharsets.UTF_8.toString())
                navController.navigate(route = "${Destination.DetailsScreen.route}/$encoded")
            })
        }

        animatedComposable(
            route = Destination.DetailsScreen.routeWithArgs,
            arguments = Destination.DetailsScreen.arguments,
            enterTransition = { enterAnim },
            exitTransition = { exitAnim }
        ) {
            RepoDetailsScreen()
        }
    }
}
