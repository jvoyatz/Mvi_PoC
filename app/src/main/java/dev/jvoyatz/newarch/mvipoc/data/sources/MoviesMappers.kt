package dev.jvoyatz.newarch.mvipoc.data.sources

import dev.jvoyatz.newarch.mvipoc.data.sources.local.MovieEntity
import dev.jvoyatz.newarch.mvipoc.data.sources.remote.MovieDto
import dev.jvoyatz.newarch.mvipoc.domain.Movie

object MoviesMappers {
    fun List<MovieDto>.dtoToMovies() = this.map { it.toMovie() }

    private fun MovieDto.toMovie() = Movie(
        title = this.original_title,
        poster = this.poster_path,
        overview = this.overview
    )

    fun List<MovieDto>.toEntities() = this.map { it.toEntity() }

    private fun MovieDto.toEntity() = MovieEntity(
        null,
        this.original_title,
        this.poster_path,
        this.overview
    )

    fun MovieEntity.toMovie() = Movie(
        title = this.title,
        poster = this.poster,
        overview = this.overview
    ).apply {
        this.id = this@toMovie.id ?: 1
    }

    fun List<MovieEntity>.entityToMovies() = this.map { it.toMovie() }
}