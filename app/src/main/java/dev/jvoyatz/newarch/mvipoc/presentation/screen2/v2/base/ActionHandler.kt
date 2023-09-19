package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.base

/**
 * handles all the actions made by the user and returns a Flow of ActionResult object
 *
 * why using a flow?
 */
interface ActionHandler<Action, PartialState, Effect> {
    operator fun invoke(action: Action): kotlinx.coroutines.flow.Flow<ActionResult<PartialState?, Effect?>>
}

