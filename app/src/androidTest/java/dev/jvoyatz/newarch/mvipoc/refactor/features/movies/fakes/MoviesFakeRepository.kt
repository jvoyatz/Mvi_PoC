package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.fakes

import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.Outcome
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie
import dev.jvoyatz.newarch.mvipoc.refactor.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesFakeRepository  @Inject constructor(): MoviesRepository {
    override suspend fun getMovies(position: Int): Outcome<List<Movie>> {
        return Outcome.Success(listOf())
    }
}