package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.example_viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jvoyatz.newarch.mvipoc.di.AppFactory
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.MviMoviesActionHandler
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.MviMoviesReducer
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.UiEffectV2
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.UiAction
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.UiStateV2
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.base.getModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * our viewmodel is not responsible for handling the intents or the final ui state
 *
 * mvi:
 *  1. view
 *      triggers an action
 *              2. intent
 *                  triggers business logic code and
 *                  generates a result which changes the state
 *                          3. model reflects our new state
 *                              which is emitted to the flow observed by the view
 *
 *    https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTo2apqAWjPeARYMpyfuUgreTs8QLhcV4CiBw&usqp=CAU
 *
 */
class MviReduceViewModelV4(
    initUiState: UiStateV2 = UiStateV2(isIdle = true),
    private val reducer: MviMoviesReducer,
    private val actionHandler: MviMoviesActionHandler,
): ViewModel() {
    private val model by getModel(actionHandler, reducer, AppFactory.dispatcherProvider, initUiState)
    val uiState: Flow<UiStateV2> = model.uiState
    val effectFlow: Flow<UiEffectV2> = model.effect

    init {
        viewModelScope.launch {
            delay(500)
            onAction(UiAction.GetMovies(1))
        }
    }
    fun onAction(action: UiAction) {
        model.mapAction(action)
    }
}