package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract

import android.os.Parcelable
import dev.jvoyatz.newarch.mvipoc.refactor.core.presentation.UiState
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class MoviesState(
    val isLoading: Boolean = false,
    val movies: @RawValue List<Movie> ? = null,
    val error: String ? = null
): UiState, Parcelable {
    override fun toString(): String {
        return "MoviesState(isLoading=$isLoading, movies=${movies?.size}, error=$error)"
    }
}