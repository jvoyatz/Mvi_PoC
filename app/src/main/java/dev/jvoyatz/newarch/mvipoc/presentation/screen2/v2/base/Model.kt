package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.base

import dev.jvoyatz.newarch.mvipoc.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * in this class we have inserted all the logic for handling (or mapping) actions
 * to business logic and the handling for returning the new ui state
 *
 * our viewmodel now has less responsibilities
 */
class Model<State, Action, PartialState, Effect>(
    private val reducer: Reducer<PartialState, State>,
    private val actionHandler: ActionHandler<Action, PartialState, Effect>,
    private val dispatcherProvider: DispatcherProvider,
    private val _effect: kotlinx.coroutines.channels.Channel<Effect>,
    private val _uiState: MutableStateFlow<State>,
    private val coroutineScope: CoroutineScope,
) {
    val uiState: StateFlow<State>
        get() = _uiState

    val effect: Flow<Effect> = _effect.receiveAsFlow()

    fun mapAction(action: Action) {
        Timber.w("mapAction() called with: action = " + action)
        coroutineScope.launch(dispatcherProvider.main) {
            Timber.w("mapAction() called with: action = " + action)
            actionHandler(action)
                .collect {
                    Timber.d("collecting and preparing for emissions !!!!!!!!!!!!")
                    it.partialState?.let { updateState(it) }
                    it.effect?.let { _effect.send(it) }
                }
        }
    }


    private fun updateState(partialState: PartialState) {
        _uiState.update {
            reducer.invoke(partialState, it)
        }
    }
}
