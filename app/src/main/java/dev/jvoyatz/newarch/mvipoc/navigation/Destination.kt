package dev.jvoyatz.newarch.mvipoc.navigation

sealed interface Destination {
    data object Movies: Destination
    data class MovieDetails(val id: Int) : Destination
}