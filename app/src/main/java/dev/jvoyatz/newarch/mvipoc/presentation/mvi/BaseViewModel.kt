package dev.jvoyatz.newarch.mvipoc.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<State: UiState, Event: UiEvent, Effect: UiEffect>(
    initialState: State
): ViewModel(){

    companion object{
        private const val LOG_TAG = "VIEWMODEL"
        private const val LOG_MSG_EVENT = "Event: %s"
        private const val LOG_MSG_STATE = "State: %s"
        private const val LOG_MSG_EFFECT = "Effect: %s"
    }

    private val state: MutableStateFlow<State> = MutableStateFlow(initialState)
    private val effect: Channel<Effect> = Channel<Effect>()

    //using sharedflow, because event is dropped in case there is not any subscriber
    private val _event : MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()


    init {
        subscribeEvents()
    }

    //helper
    protected val currentState: State
        get() = state.value

    fun state(): StateFlow<State> = state

    protected fun setState(reduce: State.() -> State) {
        val newState = state.value.reduce()
        log(newState)
        state.update { newState }
    }

    fun effect(): Flow<Effect> = effect.receiveAsFlow()

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        log(effectValue)
        viewModelScope.launch { effect.send(effectValue) }
    }

    fun postEvent(event : Event) {
        val newEvent = event
        viewModelScope.launch {
            _event.emit(newEvent)
        }
    }

    private fun subscribeEvents(){
        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }
    }

    abstract fun handleEvent(event: Event)

    private fun log(event: Event) = Timber.tag(LOG_TAG).d(LOG_MSG_EVENT, event)
    private fun log(state: State) = Timber.tag(LOG_TAG).d(LOG_MSG_STATE, state)
    private fun log(effect: Effect) = Timber.tag(LOG_TAG).d(LOG_MSG_EFFECT, effect)
}