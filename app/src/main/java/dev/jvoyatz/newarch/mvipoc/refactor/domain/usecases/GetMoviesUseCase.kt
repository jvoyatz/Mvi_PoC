package dev.jvoyatz.newarch.mvipoc.refactor.domain.usecases

import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.Outcome
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie
import dev.jvoyatz.newarch.mvipoc.refactor.domain.repository.MoviesRepository

class UseCaseTest
fun interface GetMoviesUseCase: suspend (Int) -> Outcome<List<Movie>> {
    companion object {
        fun create(repository: MoviesRepository) = GetMoviesUseCase(repository::getMovies)
    }
}