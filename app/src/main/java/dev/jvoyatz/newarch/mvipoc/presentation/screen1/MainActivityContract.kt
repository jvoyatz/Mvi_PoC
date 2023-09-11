package dev.jvoyatz.newarch.mvipoc.presentation.screen1

import dev.jvoyatz.newarch.mvipoc.domain.Movie
import dev.jvoyatz.newarch.mvipoc.presentation.mvi.UiEffect
import dev.jvoyatz.newarch.mvipoc.presentation.mvi.UiEvent

object MainActivityContract {

    /**
     * State is a data class that
     * corresponds to the actual state of UI Elements
     */
    data class UiState(
        val mainViewState: MainViewState,
    ) : dev.jvoyatz.newarch.mvipoc.presentation.mvi.UiState

    /**
     * holds different states
     */
    sealed class MainViewState {
        object Init : MainViewState()
        object Loading : MainViewState()
        data class Results(val movies: List<Movie>) : MainViewState()
        object NoResults : MainViewState()
    }

    sealed class Event : UiEvent {
        object Init : Event()
        data class GetMovies(val page: Int) : Event()
    }

    /**
     * A simple action that it is shown once
     */
    sealed class Effect : UiEffect {
        object ShowError : Effect()
    }
}