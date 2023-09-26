package dev.jvoyatz.newarch.mvipoc.refactor.core.navigation

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.MovieListFragmentDirections
import javax.inject.Inject

/**
 * Our goal is to to use viewmodel navigation instead of injecting our navigator into each screen
 * Since we can't inject NavController with Hilt in this class because NavController lives on ActivityComponent
 * and we need to use this class in ViewModel classes, we are not able to achieve due to the different scopes
 * of each class (Activity, ViewModel).
 *
 * So, we provide two methods to setup our NavControllers and clear them as well,
 * when an activity is (re)created or destroyed
 */
class AppNavigatorImpl @Inject constructor() : AppNavigator {

    private var navController: NavController? = null
    private var fragmentManager: FragmentManager? = null

    override fun navigate(destination: AppDestination) {
        when (destination) {
            is AppDestination.NavGraphDestination.MovieDetails -> navController?.navigate(
                MovieListFragmentDirections.actionMoviesListFragmentToMoviesDetailsFragment()
            )

            AppDestination.ExplicitDestination.ManualNavDestination1 -> TODO()
            AppDestination.ExplicitDestination.ManualNavDestination2 -> TODO()
            AppDestination.NavGraphDestination.Back -> navController?.popBackStack()
        }
    }

    override fun init(block: () -> Pair<NavController, FragmentManager>) {
        val (navController, supportFragmentManager) = block()
        this.navController = navController
        this.fragmentManager = supportFragmentManager
    }

    override fun clear() {
        navController = null
        fragmentManager = null
    }
}
