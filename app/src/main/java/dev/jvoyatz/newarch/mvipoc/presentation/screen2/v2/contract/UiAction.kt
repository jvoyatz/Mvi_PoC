package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract

sealed interface UiAction {
    data class GetMovies(val page: Int) : UiAction
    data object OnMovieSelection: UiAction
}