package dev.jvoyatz.newarch.mvipoc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.core.util.Preconditions
import androidx.fragment.R
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider

@AndroidEntryPoint
class HiltTestActivity: AppCompatActivity() {
}


object Ext {
    /**
     * launchFragmentInContainer from the androidx.fragment:fragment-testing library
     * is NOT possible to use right now as it uses a hardcoded Activity under the hood
     * (i.e. [EmptyFragmentActivity]) which is not annotated with @AndroidEntryPoint.
     *
     * As a workaround, use this function that is equivalent. It requires you to add
     * [HiltTestActivity] in the debug folder and include it in the debug AndroidManifest.xml file
     * as can be found in this project.
     */
    @SuppressLint("RestrictedApi")
    inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        fragmentArgs: Bundle? = null,
        @StyleRes themeResId: Int = androidx.fragment.testing.manifest.R.style.FragmentScenarioEmptyFragmentActivityTheme,
        crossinline action: Fragment.() -> Unit = {}
    ) {
        val startActivityIntent = Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                HiltTestActivity::class.java
            )
        )

        ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
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