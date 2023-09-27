package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.jvoyatz.newarch.mvipoc.Ext
import dev.jvoyatz.newarch.mvipoc.R
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MovieListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init(){
        hiltRule.inject()
    }
    @Test
    fun testEventFragment() {
        Ext.launchFragmentInHiltContainer<MovieListFragment>()
        ViewMatchers.isRoot().run {
            object : ViewAction{
                override fun getDescription(): String = "test get description".also { println(it) }
                override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
                override fun perform(uiController: UiController?, view: View?) {
                    uiController!!.loopMainThreadForAtLeast(5000)
                }
            }.also {
                Espresso.onView(ViewMatchers.isRoot()).perform(it)
                Espresso.onView(withId(R.id.noResults)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            }

        }
    }
}
