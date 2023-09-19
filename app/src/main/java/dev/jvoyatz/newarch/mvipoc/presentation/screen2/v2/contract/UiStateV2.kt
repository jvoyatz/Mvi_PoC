package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract

import dev.jvoyatz.newarch.mvipoc.domain.Movie

/**
 * Choosing sealed for your states representations seem to be an elegant choice, since they offer better organization, type safety
 * and easier navigation/handling/tracking between the states and work well with complex business logic
 *
 * however, using a simple data class or a flat representation is quite straightforward
 * it is also easy to understand and update, you don't have as well to check for subclasses and
 * it fits better for jetpack compose
 *
 */
data class UiStateV2 ( //it's time to use a different name - ViewState?
    val isIdle: Boolean = false,
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val hasMovies: Boolean = false,
    val hasError: Boolean = false,
    val error: String = ""
)