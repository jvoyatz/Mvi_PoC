package dev.jvoyatz.newarch.mvipoc.presentation.screen2

import androidx.lifecycle.SavedStateHandle
import dev.jvoyatz.newarch.mvipoc.domain.GetMoviesUseCaseV3
import dev.jvoyatz.newarch.mvipoc.outcome.Outcome
import dev.jvoyatz.newarch.mvipoc.presentation.mvi.BaseViewModelV3
import dev.jvoyatz.newarch.mvipoc.presentation.screen1.FIRST_PAGE
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.UiMapper.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

class MviReduceViewModel constructor(
    private val getMoviesUseCase: GetMoviesUseCaseV3,
    savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : BaseViewModelV3<MviReduceContract.MviReduceState, MviReduceContract.PartialState, MviReduceContract.Event, MviReduceContract.Effect>(
    savedStateHandle,
    MviReduceContract.MviReduceState()
) {

    init {
        postEvent(MviReduceContract.Event.Init)
    }
    override fun handleEvent(event: MviReduceContract.Event): Flow<MviReduceContract.PartialState> {
        return when (event) {
            is MviReduceContract.Event.Init -> getMovies(FIRST_PAGE)
            is MviReduceContract.Event.GetMovies -> getMovies(event.page)
            MviReduceContract.Event.OnMovieSelection -> onMovieClicked()
        }
    }

    override fun reduceUiState(
        prevState: MviReduceContract.MviReduceState,
        partialState: MviReduceContract.PartialState
    ): MviReduceContract.MviReduceState {
        return when (partialState) {
            is MviReduceContract.PartialState.Error -> prevState.copy(
                isLoading = false,
                isError = true,
                movies = listOf()
            ).also {
                Timber.d("isError")
            }

            is MviReduceContract.PartialState.FetchedMovies -> prevState.copy(
                isLoading = false,
                isError = false,
                movies = partialState.list
            ).also {
                Timber.d("fetchedMovies ${partialState.list.size}")
            }

            MviReduceContract.PartialState.Loading -> prevState.copy(
                isLoading = false,
                isError = false,
                movies = listOf()
            ).also {
                Timber.d("fetchedMovies isLoading = true")
            }

            MviReduceContract.PartialState.NoResults -> prevState.copy(
                isLoading = false,
                isError = false,
                movies = listOf()
            ).also {
                Timber.d("noResults")
            }
        }
    }

    private fun getMovies(position: Int): Flow<MviReduceContract.PartialState> =
        getMoviesUseCase(position)
            .map {

                if (it is Outcome.Success && it.value != null) {
                    MviReduceContract.PartialState.FetchedMovies(it.value.map { it.toUiModel() }!!)
                } else if (it is Outcome.Success) {
                    MviReduceContract.PartialState.NoResults
                } else {
                    MviReduceContract.PartialState.Error("error")
                }
            }
            .onStart {
                emit(MviReduceContract.PartialState.Loading)
            }

    private fun onMovieClicked(): Flow<MviReduceContract.PartialState>{

        setEffect { MviReduceContract.Effect.ShowError }

        return emptyFlow()
    }
}