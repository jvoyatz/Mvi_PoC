package dev.jvoyatz.newarch.mvipoc.presentation.screen1

import androidx.lifecycle.viewModelScope
import dev.jvoyatz.newarch.mvipoc.presentation.screen1.MainActivityContract.MainViewState
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.onError
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.onSuccess
import dev.jvoyatz.newarch.mvipoc.domain.GetMoviesUseCase
import dev.jvoyatz.newarch.mvipoc.presentation.mvi.BaseViewModel
import kotlinx.coroutines.launch

const val FIRST_PAGE = 1

class MainViewModel constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    initViewState: MainViewState
) : BaseViewModel<MainActivityContract.UiState, MainActivityContract.Event, MainActivityContract.Effect>(
    MainActivityContract.UiState(initViewState)
) {

    override fun processEvent(event: MainActivityContract.Event) {
        when(event){
            is MainActivityContract.Event.Init -> getMovies(FIRST_PAGE)
            is MainActivityContract.Event.GetMovies -> getMovies(event.page)
        }
    }

    private fun getMovies(position: Int) {
        setState { this.copy(mainViewState = MainViewState.Loading) }

        viewModelScope.launch {
            getMoviesUseCase(position)
                .onSuccess {
                    it?.let { movies ->
                        setState { copy(mainViewState = MainViewState.Results(movies)) }
                    } ?: kotlin.run {
                        setState { copy(mainViewState = MainViewState.NoResults) }
                    }
                }.onError {
                    setEffect { MainActivityContract.Effect.ShowError }
                }
        }
    }
}