package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract

sealed interface UiEffectV2 {
    data class ShowErrorToast(val message: String): UiEffectV2
}