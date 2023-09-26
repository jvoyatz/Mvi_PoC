package dev.jvoyatz.newarch.mvipoc.refactor.data


import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.Outcome
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.OutcomeExtensions.mapSuccess
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.OutcomeExtensions.onError
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.OutcomeExtensions.outcomeFromApiCall
import dev.jvoyatz.newarch.mvipoc.refactor.data.MoviesMappers.dtoToMovies
import dev.jvoyatz.newarch.mvipoc.refactor.data.remote.MoviesApi
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie
import dev.jvoyatz.newarch.mvipoc.refactor.domain.repository.MoviesRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

const val API_KEY = "e8d648003bd11b5c498674fbd4905525"

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val apiService: MoviesApi
) : MoviesRepository {

    private val list = mutableListOf<Movie>()

    override suspend fun getMovies(position: Int): Outcome<List<Movie>> {
        return outcomeFromApiCall {
            apiService.getTopRatedMovies(API_KEY, "en-US", position)
        }.mapSuccess {
            this.results.dtoToMovies().also {
                list.addAll(it)
            }
            list
        }.onError {
            Outcome.Error("caught an api error")
        }
    }
}