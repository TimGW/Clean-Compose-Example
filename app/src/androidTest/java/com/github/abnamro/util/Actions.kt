package com.github.abnamro.util

import androidx.test.espresso.IdlingRegistry

internal fun IdlingRegistry.waitFor(waitingTime: Long = 50, action: (Boolean) -> Unit) {
    val idlingResource = ElapsedTimeIdlingResource(waitingTime)
    register(idlingResource)

    action.invoke(idlingResource.isIdleNow)

    unregister(idlingResource)
}
