package com.pixabay.imageloaderapp

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pixabay.imageloaderapp.presentation.activity.ImageLoaderHomeScreenActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageLoaderFragmentTest {

    @Rule
    @JvmField
    var activityScenarioRule = ActivityScenarioRule(ImageLoaderHomeScreenActivity::class.java)

    @Test
    fun recyclerViewIsDisplayed() {
        onView(withId(R.id.recyclerView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun searchViewIsShowing() {
        onView(withId(R.id.searchView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun cancelImageVisibility() {
        runBlocking {
            onView(withId(R.id.searchView))
                .perform(click())
                .perform(pressKey(KeyEvent.KEYCODE_DEL))
                .perform(pressKey(KeyEvent.KEYCODE_DEL))
                .perform(pressKey(KeyEvent.KEYCODE_DEL))
                .perform(pressKey(KeyEvent.KEYCODE_DEL))
                .perform(pressKey(KeyEvent.KEYCODE_DEL))
                .perform(pressKey(KeyEvent.KEYCODE_DEL))
            onView(withId(R.id.cancel_search)).check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        }
    }

    @Test
    fun emptySectionIsShowing() {
        runBlocking {
            onView(withId(R.id.searchView)).perform(replaceText("dummyvalue")).perform(
                pressImeActionButton()
            )
            delay(5000)
            onView(withId(R.id.empty_section)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }
}