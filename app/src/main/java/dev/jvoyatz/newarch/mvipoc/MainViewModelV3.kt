package dev.jvoyatz.newarch.mvipoc

import androidx.lifecycle.SavedStateHandle
import dev.jvoyatz.newarch.mvipoc.UiMapper.toUiModel
import dev.jvoyatz.newarch.mvipoc.domain.GetMoviesUseCaseV2
import dev.jvoyatz.newarch.mvipoc.outcome.Outcome
import dev.jvoyatz.newarch.mvipoc.presentation.mvi.BaseViewModelV3
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


class MainViewModelV3 constructor(
    private val getMoviesUseCase: GetMoviesUseCaseV2,
    savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : BaseViewModelV3<MainActivityContract.MainViewStateV3, MainActivityContract.PartialState, MainActivityContract.Event, MainActivityContract.Effect>(
    savedStateHandle,
    MainActivityContract.MainViewStateV3()
) {

    override fun handleEvent(event: MainActivityContract.Event): Flow<MainActivityContract.PartialState> {
        return when(event){
            is MainActivityContract.Event.Init -> getMovies(FIRST_PAGE)
            is MainActivityContract.Event.GetMovies -> getMovies(event.page)
        }
    }

    override fun reduceUiState(
        prevState: MainActivityContract.MainViewStateV3,
        partialState: MainActivityContract.PartialState
    ): MainActivityContract.MainViewStateV3 {
        return when(partialState){
            is MainActivityContract.PartialState.Error -> prevState.copy(isLoading = false, isError = true, movies = listOf())
            is MainActivityContract.PartialState.FetchedMovies -> prevState.copy(isLoading = false, isError = false, movies = partialState.list)
            MainActivityContract.PartialState.Loading -> prevState.copy(isLoading = false, isError = false, movies = listOf())
            MainActivityContract.PartialState.NoResults -> prevState.copy(isLoading = false, isError = false, movies = listOf())
        }
    }

    private fun getMovies(position: Int): Flow<MainActivityContract.PartialState> =
        getMoviesUseCase(position)
            .map {
                if(it is Outcome.Success && it.value != null){
                    MainActivityContract.PartialState.FetchedMovies(it.value.map { it.toUiModel() }!!)
                } else if(it is Outcome.Success){
                    MainActivityContract.PartialState.NoResults
                } else {
                    MainActivityContract.PartialState.Error("error")
                }
            }
            .onStart {
                emit(MainActivityContract.PartialState.Loading)
            }
}