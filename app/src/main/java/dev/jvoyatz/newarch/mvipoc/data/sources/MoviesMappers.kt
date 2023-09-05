package dev.jvoyatz.newarch.mvipoc.data.sources

import gr.jvoyatz.android.poc.mvi.domain.Movie

object MoviesMappers {
    fun List<MovieDto>.toMovies() = this.map { it.toMovie() }

    private fun MovieDto.toMovie() = Movie(
        title = this.original_title,
        poster = this.poster_path,
        overview = this.overview
    )
}