package dev.jvoyatz.newarch.mvipoc.data

import dev.jvoyatz.newarch.mvipoc.data.sources.MoviesMappers.toEntities
import dev.jvoyatz.newarch.mvipoc.outcome.Outcome
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.mapSuccess
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.onError
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.outcomeFromApiCall
import dev.jvoyatz.newarch.mvipoc.data.sources.remote.MoviesApiService
import dev.jvoyatz.newarch.mvipoc.data.sources.MoviesMappers.dtoToMovies
import dev.jvoyatz.newarch.mvipoc.data.sources.MoviesMappers.entityToMovies
import dev.jvoyatz.newarch.mvipoc.data.sources.local.MoviesDao
import dev.jvoyatz.newarch.mvipoc.di.AppFactory
import dev.jvoyatz.newarch.mvipoc.domain.Movie
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.toSuccessfulOutcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

const val API_KEY = "e8d648003bd11b5c498674fbd4905525"

class MoviesRepository(
    private val apiService: MoviesApiService
) {
    //in-memory cache
    private val list = mutableListOf<Movie>()


    private val moviesDao: MoviesDao = AppFactory.moviesDao

    suspend fun getMovies(position: Int): Outcome<List<Movie>> {
        return outcomeFromApiCall {

            if(position == 3) throw RuntimeException("an error")

            apiService.getTopRatedMovies(API_KEY, "en-US", position)
        }.mapSuccess {
            this.results.dtoToMovies().also {
                list.apply {
                    addAll(it)
                }
                list
            }
        }
        .onError {
            Outcome.Error("caught an api error")
        }
    }

    fun getMoviesFlow(position: Int): Flow<Outcome<List<Movie>>> {
        return moviesDao.getMovies()
            .map {
                it.entityToMovies()
            }.onEach {
                if (it.isEmpty() ) {
                    apiService.getTopRatedMovies2(API_KEY, "en-US", position)
                        .results.toEntities().also {
                            moviesDao.insertMovies(it)
                        }
                }
            }.map {
                it.toSuccessfulOutcome()
            }
            .onEach {
                Timber.d("emit!! $it")
            }
    }
}