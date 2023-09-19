package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jvoyatz.newarch.mvipoc.DispatcherProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * delegate used to get an instance of model
 */
class ModelDelegate<State, Action, PartialState, Effect>(
    private val viewModel: ViewModel,
    private val actionHandler: ActionHandler<Action, PartialState, Effect>,
    private val reducer: Reducer<PartialState, State>,
    private val _effect: kotlinx.coroutines.channels.Channel<Effect>,
    private val _uiState: MutableStateFlow<State>,
    private val dispatcherProvider: DispatcherProvider
) : ReadOnlyProperty<Any, Model<State, Action, PartialState, Effect>> {
    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): Model<State, Action, PartialState, Effect> {
        return Model(
            reducer = reducer,
            actionHandler = actionHandler,
            _effect = _effect,
            _uiState = _uiState,
            coroutineScope = viewModel.viewModelScope,
            dispatcherProvider = dispatcherProvider
        )
    }
}

fun <UiState, Action, PartialState, Effect> ViewModel.getModel(
    actionHandler: ActionHandler<Action, PartialState, Effect>,
    reducer: Reducer<PartialState, UiState>,
    dispatcherProvider: DispatcherProvider,
    initState: UiState
): ModelDelegate<UiState, Action, PartialState, Effect> {
    return ModelDelegate(
        actionHandler = actionHandler,
        reducer = reducer,
        dispatcherProvider = dispatcherProvider,
        _uiState = MutableStateFlow(initState),
        _effect = Channel(Channel.BUFFERED),
        viewModel = this
    )
}