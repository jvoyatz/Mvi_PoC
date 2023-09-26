package dev.jvoyatz.newarch.mvipoc.refactor.core.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.properties.ReadWriteProperty

/**
 * Base ViewModel class which implements the MVI flow.
 *  - Uses a StateFlow to expose [State] objects representing the single state for a View
 *  - Uses channels for one time events -- see [Event]
 *  - Exposes a method  - see [BaseViewModel.onAction] - to execute the predefined actions for a particular View. See [Action] as well.
 *  - Defines an abstract method [BaseViewModel.mapAction] which is responsible to map a given action passed through [BaseViewModel.onAction] method
 *
 */
abstract class BaseViewModel<State: UiState, Action: UiAction, Mutation: UiMutation<State>, Event: UiEvent>(
    private val savedStateHandle: SavedStateHandle,
    private val savedStateKey: String,
    initialState: State
): ViewModel(){
    init {
        Timber.d("savedStateHandle ${savedStateHandle.get<State>(savedStateKey)}")
    }
//    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
//    val state = _uiState.asStateFlow()
    val state = savedStateHandle.getStateFlow(savedStateKey, initialState)
    private val _events: Channel<Event> = Channel(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    /**
     * Updates the StateFlow which exposes the state exposed to a View
     */
    private fun setState(mutation: Mutation) {
        println("mutation $mutation")
        log(mutation)
        with(mutation.reduce(state.value)){
            println("mutation $this")
            savedStateHandle[savedStateKey] = this
            //_uiState.update { state }
            log(this)
        }
    }

    /**
     * Emits a new event in the dedicated channel for One-Time events.
     */
    private fun emitEvent(event: Event) {
        log(event)
        viewModelScope.launch {
            _events.send(event)
        }
    }

    /**
     * This method is invoked from the View when a user decides to
     * execute a certain [Action] (or the I(ntent) in MVI terminology).
     */
    fun onAction(action: Action) {
        viewModelScope.launch {
            log(action)
            mapAction(action).collect { holder ->
                log(holder)
                with(holder) {
                    event?.let { emitEvent(it) }
                    mutation?.let { setState(it) }
                }
            }
        }
    }

    /**
     * Will be implemented in each subclass.
     * It's purpose is to map a certain actions to its corresponding business logic
     *
     *
     * This method returns a Flow object, why returning Flow?
     *      Because someone might wants to send more than one emissions in this Stream.
     *
     * This Flow contains the type [UiMutationPair], what exatly is this type?
     * Just a simple Pair class, that contains two important info fields for our implementation.
     * Those two fields, in case they are not null, let us know what we should update on our exposed (to the View) state or
     * if we need to display an One-Time-Event.
     */
    abstract fun mapAction(action: Action): Flow<UiMutationPair<Mutation?, Event?>>


    //helpers for logging
    companion object{
        private const val LOG_TAG = "VIEWMODEL"
        private const val LOG_MSG_ACTION = "Action --> [%s]"
        private const val LOG_MSG_ACTION_MUTATION = "Action Mutation --> [%s]"
        private const val LOG_MSG_ACTION_MUTATION_PAIR = "Action Mutation Pair --> [%s]"
        private const val LOG_MSG_STATE = "State --> [%s]"
        private const val LOG_MSG_EVENT = "Event --> [%s]"

    }
    private fun log(action: Action) = Timber.tag(LOG_TAG).d(LOG_MSG_ACTION, action)
    private fun log(mutation: UiMutation<State>) = Timber.tag(LOG_TAG).d(LOG_MSG_ACTION_MUTATION, "new mutation --> [${mutation}]")
    private fun log(state: State) = Timber.tag(LOG_TAG).d(LOG_MSG_STATE, state)
    private fun log(event: Event) = Timber.tag(LOG_TAG).d(LOG_MSG_EVENT, event)
    private fun log(pair: UiMutationPair<Mutation?, Event?>) = Timber.tag(LOG_TAG).d(LOG_MSG_ACTION_MUTATION_PAIR, pair)
}