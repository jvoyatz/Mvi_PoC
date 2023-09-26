package dev.jvoyatz.newarch.mvipoc.fakes

import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.Outcome
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie
import dev.jvoyatz.newarch.mvipoc.refactor.domain.repository.MoviesRepository

class FakeMoviesRepository: MoviesRepository {

    var moviesOutcome: Outcome<List<Movie>> ?= null
    override suspend fun getMovies(position: Int): Outcome<List<Movie>> = moviesOutcome!!
}