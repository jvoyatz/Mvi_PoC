package dev.jvoyatz.newarch.mvipoc.presentation.screen2

import android.os.Parcelable
import dev.jvoyatz.newarch.mvipoc.domain.Movie
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieUiModel(
    val title: String, val poster: String, val overview: String,
    val id: Int = -1
) : Parcelable


object UiMapper{
    fun Movie.toUiModel() = MovieUiModel(
        this.title,
        this.poster,
        this.overview,
        this.id
    )
}