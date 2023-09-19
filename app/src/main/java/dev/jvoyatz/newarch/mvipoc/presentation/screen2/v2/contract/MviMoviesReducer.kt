package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract

import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.base.Reducer

class MviMoviesReducer: Reducer<MoviesPartialStateV2, UiStateV2> {
    override fun invoke(partialState: MoviesPartialStateV2, state: UiStateV2): UiStateV2 {
        return when(partialState) {
            MoviesPartialStateV2.ShowLoading -> partialState.reduce(state)
            is MoviesPartialStateV2.ShowContent -> partialState.reduce(state) //1st way
            is MoviesPartialStateV2.ShowError -> state.toError(partialState.error)// 2nd way
            MoviesPartialStateV2.ShowEmpty -> partialState.reduce(state)
        }
    }

    private fun UiStateV2.toError(error: String): UiStateV2 {
        return copy(
            isIdle = false,
            isLoading = false,
            hasError = true,
            hasMovies = false,
            error = error
        )
    }
}