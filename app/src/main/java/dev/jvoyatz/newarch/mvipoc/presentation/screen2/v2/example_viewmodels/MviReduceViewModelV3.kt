package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.example_viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.MviMoviesActionHandler
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

/**
 * fewer responsibilities, few dependencies
 * delegates its tasks to the handler & the reducer
 */
class MviReduceViewModelV3(
    initUiState: UiStateV2 = UiStateV2(),
    private val reducer: MviMoviesReducer,
    private val actionHandler: MviMoviesActionHandler,
): ViewModel() {
    private val _uiState = MutableStateFlow<UiStateV2>(initUiState)
    private val _effect: Channel<UiEffectV2> = Channel(BUFFERED)

    val uiState: Flow<UiStateV2> = _uiState
    val effectFlow: Flow<UiEffectV2> = _effect.receiveAsFlow()


    fun onAction(action: UiAction){
        viewModelScope.launch {
            actionHandler(action).collect { actionResult ->
                with(actionResult) {
                    this.partialState?.let { updateState(it) }
                    this.effect?.let { _effect.trySend(it) }
                }
            }
        }
    }

    private fun updateState(partialState: MoviesPartialStateV2){
        _uiState.update {
            reducer.invoke(partialState, it)
        }
    }

}