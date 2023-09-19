package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.base

//fun interface PartialStateReducerV2<Action, UiState>: (Action, UiState) -> UiState


/**
 * a function that takes the current state and the result of a certain action, let's call it
 * partial state as argument, which is applied to the current state and thus, a new state is returned
 *
 * State is updated given the user's actions and the other update from the business logic
 */
interface Reducer<Transformation, UiState>{
    operator fun invoke(transformation: Transformation, state: UiState): UiState
}