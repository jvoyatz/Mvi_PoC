package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract

import dev.jvoyatz.newarch.mvipoc.domain.Movie

/**
 * DOCUMENTATION TO BE ADDED
 */
sealed interface MoviesPartialStateV2 {

    fun reduce(state: UiStateV2): UiStateV2
    data object ShowLoading: MoviesPartialStateV2 {
        override fun reduce(state: UiStateV2): UiStateV2 = state.copy(isLoading = true, isIdle = false)
    }
    data class ShowContent(val movies: List<Movie>) : MoviesPartialStateV2 {
        override fun reduce(state: UiStateV2): UiStateV2 = state.copy(
            isIdle = false,
            isLoading = false,
            hasError = false,
            hasMovies = true,
            movies = this.movies
        )
    }

    data object ShowEmpty: MoviesPartialStateV2 {
        override fun reduce(state: UiStateV2): UiStateV2 = state.copy(
            isIdle = false,
            isLoading = false,
            hasMovies = true,
            movies = listOf(),
            hasError = false
        )

    }
    data class ShowError(val error: String = "unexpected error"): MoviesPartialStateV2 {
        override fun reduce(state: UiStateV2): UiStateV2 = state.copy(
            isIdle = false,
            isLoading = false,
            hasError = true,
            hasMovies = false,
            error = this.error
        )
    }
}