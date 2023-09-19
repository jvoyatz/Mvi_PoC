package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.example_viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jvoyatz.newarch.mvipoc.domain.GetMoviesUseCase
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.onError
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.onSuccess
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.MviMoviesReducer
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.MoviesPartialStateV2
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.UiEffectV2
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.UiAction
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.UiStateV2
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MviReduceViewModelV2(
    private val getMoviesUseCase: GetMoviesUseCase,
    initUiState: UiStateV2 = UiStateV2(),
    private val reducer: MviMoviesReducer
): ViewModel() {
    private val _uiState = MutableStateFlow<UiStateV2>(initUiState)
    private val _effect: Channel<UiEffectV2> = Channel(BUFFERED)

    val uiState: Flow<UiStateV2> = _uiState
    val effectFlow: Flow<UiEffectV2> = _effect.receiveAsFlow()


    fun onAction(event: UiAction){
        when(event) {
            is UiAction.GetMovies -> getMovies(event.page)
            is UiAction.OnMovieSelection -> {
                //getMovie(...)
                Unit
            }
        }
    }

    private fun getMovies(position: Int) {
        viewModelScope.launch {
            updateState(MoviesPartialStateV2.ShowLoading)
            getMoviesUseCase(position)
                .onSuccess {
                    it?.let { movies ->
                        updateState(MoviesPartialStateV2.ShowContent(movies))
                    } ?: kotlin.run {
                        updateState(MoviesPartialStateV2.ShowEmpty)
                    }
                }.onError {
                    updateState(MoviesPartialStateV2.ShowError())
                }
        }
    }

    private fun updateState(partialState: MoviesPartialStateV2){
        _uiState.update {
            reducer.invoke(partialState, it)
        }
    }

}