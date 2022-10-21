package com.github.abnamro.util

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.testing.R
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions
import com.github.abnamro.HiltTestActivity

object TestHelper {

    fun createActivityScenario(
        @StyleRes themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
    ): ActivityScenario<HiltTestActivity> {
        val startActivityIntent = Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                HiltTestActivity::class.java
            )
        ).putExtra(
            "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
            themeResId
        )
        return ActivityScenario.launch(startActivityIntent)
    }

    inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        activityScenario: ActivityScenario<HiltTestActivity>,
        fragmentArgs: Bundle? = null,
        crossinline action: Fragment.() -> Unit = {}
    ): ActivityScenario<HiltTestActivity>? {
        return activityScenario.onActivity { activity ->
            val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
                Preconditions.checkNotNull(T::class.java.classLoader),
                T::class.java.name
            )
            fragment.arguments = fragmentArgs
            activity.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "")
                .commitNow()

            fragment.action()
        }
    }
}