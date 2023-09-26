package dev.jvoyatz.newarch.mvipoc.refactor.domain.repository

import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.Outcome
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie

interface MoviesRepository {
    suspend fun getMovies(position: Int): Outcome<List<Movie>>
}