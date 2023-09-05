package dev.jvoyatz.newarch.mvipoc.presentation.mvi

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

private const val SAVED_UI_STATE_KEY = "SAVED_STATE_KEY"

abstract class BaseViewModelV3<State: Parcelable, PartialState, Event, Effect>(
    private val savedStateHandle: SavedStateHandle,
    initialState: State
): ViewModel(){

    val state = savedStateHandle.getStateFlow(SAVED_UI_STATE_KEY, initialState)

    //using sharedflow, because event is dropped in case there is not any subscriber
    private val _event : MutableSharedFlow<Event> = MutableSharedFlow()

    private val effect: Channel<Effect> = Channel<Effect>()

    init {
        subscribeEvents()
    }

    private fun subscribeEvents(){
        viewModelScope.launch {
            _event.flatMapMerge {
                handleEvent(it)
            }.scan(state.value) { state, newPartialState ->
                reduceUiState(state, newPartialState)
            }
                .catch { Timber.d("exception $it") }
                .collect {
                    savedStateHandle[SAVED_UI_STATE_KEY] = it
                }
        }
    }

    fun state(): StateFlow<State> = state

    fun effect(): Flow<Effect> = effect.receiveAsFlow()

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { effect.send(effectValue) }
    }

    fun postEvent(event : Event) {
        val newEvent = event
        viewModelScope.launch {
            _event.emit(newEvent)
        }
    }

    abstract fun handleEvent(event: Event): Flow<PartialState>

    protected abstract fun reduceUiState(
        prevState: State,
        partialState: PartialState
    ): State

}