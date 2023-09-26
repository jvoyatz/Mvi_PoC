package dev.jvoyatz.newarch.mvipoc.refactor.data

import dev.jvoyatz.newarch.mvipoc.refactor.data.remote.MovieDto
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie
import kotlin.random.Random


object MoviesMappers {
    fun List<MovieDto>.dtoToMovies() = this.map { it.toMovie() }
    private fun MovieDto.toMovie() = Movie(
        title = this.original_title,
        poster = this.poster_path,
        overview = this.overview,
    ).apply {
        id = Random.nextInt()
    }
}