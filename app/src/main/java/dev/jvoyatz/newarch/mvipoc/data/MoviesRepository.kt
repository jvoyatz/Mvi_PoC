package dev.jvoyatz.newarch.mvipoc.data

import dev.jvoyatz.newarch.mvipoc.outcome.Outcome
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.mapSuccess
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.onError
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.outcomeFromApiCall
import dev.jvoyatz.newarch.mvipoc.data.sources.MoviesApiService
import dev.jvoyatz.newarch.mvipoc.data.sources.MoviesMappers.toMovies
import gr.jvoyatz.android.poc.mvi.domain.Movie

const val API_KEY = "e8d648003bd11b5c498674fbd4905525"

class MoviesRepository(
    private val apiService: MoviesApiService
) {

    //in-memory cache
    private val list = mutableListOf<Movie>()

    suspend fun getMovies(position: Int): Outcome<List<Movie>> {
        return outcomeFromApiCall {

            if(position == 3) throw RuntimeException("an error")

            apiService.getTopRatedMovies(API_KEY, "en-US", position)
        }.mapSuccess {
            this.results.toMovies().also {
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

}