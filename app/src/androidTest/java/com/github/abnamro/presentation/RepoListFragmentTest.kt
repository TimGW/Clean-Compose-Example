package com.github.abnamro.presentation

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.abnamro.R
import com.github.abnamro.presentation.repo.list.RepoListFragment
import com.github.abnamro.util.TestHelper
import com.github.abnamro.util.waitFor
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoListFragmentTest {
    private val registry = IdlingRegistry.getInstance()

    @Before
    fun setupTest() {
        TestHelper.launchFragmentInHiltContainer<RepoListFragment>(
            TestHelper.createActivityScenario()
        )
    }

    @Test
    fun repoListItemTest() {
        registry.waitFor(1000) {
            onView(withId(R.id.recyclerView))
                .check(matches(hasDescendant(withId(R.id.repo_item_name))))
                .check(matches(hasDescendant(withId(R.id.repo_item_visibility))))
                .check(matches(hasDescendant(withId(R.id.repo_item_private))))
                .check(matches(hasDescendant(withId(R.id.repo_item_image))))
        }
    }
}