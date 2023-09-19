package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.base

class ActionResult<out PartialState, out Effect>(
    val partialState: PartialState?,
    val effect: Effect?
)

