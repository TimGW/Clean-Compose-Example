package com.github.abnamro.presentation

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.abnamro.R
import com.github.abnamro.presentation.repo.details.RepoDetailsFragment
import com.github.abnamro.presentation.repo.list.RepoListFragment
import com.github.abnamro.util.TestHelper
import com.github.abnamro.util.waitFor
import org.hamcrest.core.AllOf.allOf
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoNavTest {
    private val registry = IdlingRegistry.getInstance()

    @Test
    fun testNavigationToDetailsScreen() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val activityScenario = TestHelper.createActivityScenario()

        TestHelper.launchFragmentInHiltContainer<RepoListFragment>(activityScenario) {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(requireView(), navController)
        }

        registry.waitFor(1000) {
            onView(allOf(withId(R.id.recyclerView), isDisplayed()))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
                )

            assertEquals(R.id.fragment_repo_details, navController.currentDestination?.id)
        }
    }


    @Test
    fun testNavigationBackFromDetailsScreen() {
        val fragmentArgs = bundleOf(
            "query" to "abnamrocoesd/airflow",
            "pageTitle" to "abn Amro",
        )
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val activityScenario = TestHelper.createActivityScenario()

        TestHelper.launchFragmentInHiltContainer<RepoListFragment>(activityScenario) {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(requireView(), navController)
        }

        TestHelper.launchFragmentInHiltContainer<RepoDetailsFragment>(
            activityScenario,
            fragmentArgs
        )

        pressBack()

        assertEquals(R.id.fragment_repo_list, navController.currentDestination?.id)
    }
}