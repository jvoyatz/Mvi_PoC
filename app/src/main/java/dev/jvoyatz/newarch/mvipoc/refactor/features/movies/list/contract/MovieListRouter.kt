package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract

import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppDestination
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppNavigator


/**
 * This class handles the navigation actions needed for this screen
 */
class MovieListRouter constructor(private val navigator: AppNavigator) {
    fun navigateToMovieDetails(id: Int) =
        navigator.navigate(AppDestination.NavGraphDestination.MovieDetails(id))
}

//routing actions
fun interface MovieListToDetailAction : (Int) -> Unit {
    companion object Factory {
        fun create(router: MovieListRouter) = //MovieListToDetailAction(router::navigateToMovieDetails)
        //MovieListToDetailAction { id -> router.navigateToMovieDetails(id) }
            object: MovieListToDetailAction{
                override fun invoke(p1: Int) {
                    router.navigateToMovieDetails(p1)
                }
            }
    }
}
