package com.github.abnamro.presentation

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.abnamro.R
import com.github.abnamro.presentation.repo.details.RepoDetailsFragment
import com.github.abnamro.util.TestHelper
import com.github.abnamro.util.waitFor
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoDetailsTest {
    private val registry = IdlingRegistry.getInstance()

    @Before
    fun setupTest() {
        val fragmentArgs = bundleOf(
            "query" to "abnamrocoesd/airflow",
            "pageTitle" to "abn Amro",
        )
        TestHelper.launchFragmentInHiltContainer<RepoDetailsFragment>(
            TestHelper.createActivityScenario(), fragmentArgs
        )
    }

    @Test
    fun productDetailsTest() {
        registry.waitFor(1000) {
            onView(withId(R.id.repo_details_image))
                .check(matches(isDisplayed()))

            onView(withId(R.id.repo_details_name))
                .check(matches(isDisplayed()))
                .check(matches(withSubstring("Name")))
                .check(matches(withSubstring("airflow")))

            onView(withId(R.id.repo_details_full_name))
                .check(matches(isDisplayed()))
                .check(matches(withSubstring("Full name")))
                .check(matches(withSubstring("abnamrocoesd/airflow")))

            onView(withId(R.id.repo_details_description))
                .check(matches(isDisplayed()))
                .check(matches(withSubstring("Description")))
                .check(matches(withSubstring("Apache Airflow - A platform to programmatically author, schedule, and monitor workflows")))

            onView(withId(R.id.repo_details_visibility))
                .check(matches(isDisplayed()))
                .check(matches(withSubstring("Visibility")))
                .check(matches(withSubstring("public")))

            onView(withId(R.id.repo_details_private))
                .check(matches(isDisplayed()))
                .check(matches(withSubstring("Is private")))
                .check(matches(withSubstring("false")))
        }
    }
}
