package dev.jvoyatz.newarch.mvipoc.presentation.screen2

import android.os.Parcelable
import dev.jvoyatz.newarch.mvipoc.presentation.mvi.UiEffect
import dev.jvoyatz.newarch.mvipoc.presentation.mvi.UiEvent
import kotlinx.parcelize.Parcelize

object MviReduceContract {

    sealed class Event : UiEvent {
        object Init : Event()
        data class GetMovies(val page: Int) : Event()

        object OnMovieSelection: Event()
    }

    sealed class Effect : UiEffect {
        object ShowError : Effect()
    }

    @Parcelize
    data class MviReduceUiState(
        val isLoading: Boolean = false,
        val movies: List<MovieUiModel> = emptyList(),
    ): Parcelable {
        override fun toString(): String {
            return "MviState(isLoading=$isLoading, movies=${movies.size})"
        }
    }

    sealed class PartialState{
        data object Loading: PartialState()
        data class FetchedMovies(val list: List<MovieUiModel>): PartialState()
        data object NoResults: PartialState()
        data class Error(val message :String): PartialState()
    }

}