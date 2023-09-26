package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract

import dev.jvoyatz.newarch.mvipoc.refactor.core.presentation.UiAction
import dev.jvoyatz.newarch.mvipoc.refactor.core.presentation.UiMutation
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie


/**
 * This sealed interface contains a restricted set of
 * subclasses which represent the actions that can take place
 * when a user in on Movies screen,
 */
sealed interface MoviesAction : UiAction {
    data object Initialize : MoviesAction
    data object GetMovies : MoviesAction
    data class OnMovieSelection(val id: Int) : MoviesAction
}


/**
 * This class contains all the partial mutations that can be applied
 * on the [MoviesState] while a specific action is happening
 */
sealed class MoviesActionResult : UiMutation<MoviesState> {
    data object Loading : MoviesActionResult() {
        override fun reduce(state: MoviesState) = state.copy(isLoading = true)
    }

    data object NoMovies : MoviesActionResult() {
        override fun reduce(state: MoviesState) =
            state.copy(isLoading = false, movies = listOf(), error = null)
    }

    data class Movies(val movies: List<Movie>) : MoviesActionResult() {
        override fun reduce(state: MoviesState) =
            state.copy(isLoading = false, movies = this.movies, error = null)
    }

    data class Error(val error: String = "unknown error") : MoviesActionResult() {
        override fun reduce(state: MoviesState): MoviesState =
            state.copy(isLoading = false, error = this.error)
    }
}