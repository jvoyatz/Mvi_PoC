package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract

import dev.jvoyatz.newarch.mvipoc.refactor.core.presentation.UiEvent

sealed interface MoviesEvent: UiEvent{
    data class ErrorToast(val messsage: String ?= "unknown error"): MoviesEvent
    data class OnMovieSelected(val id: Int) : MoviesEvent
}