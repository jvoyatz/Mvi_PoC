package dev.jvoyatz.newarch.mvipoc.di

import dev.jvoyatz.newarch.mvipoc.outcome.Outcome
import dev.jvoyatz.newarch.mvipoc.data.MoviesRepository
import dev.jvoyatz.newarch.mvipoc.data.sources.MoviesApiService
import dev.jvoyatz.newarch.mvipoc.domain.GetMoviesUseCase

object AppFactory {

    private val moviesApiService: MoviesApiService by lazy {
        MoviesApiService.getInstance()
    }

    private val moviesRepository by lazy {
        MoviesRepository(moviesApiService)
    }

    val getMoviesUseCase by lazy {
        GetMoviesUseCase {
            try {
                moviesRepository.getMovies(it ?: 1)
            }catch (e: Exception){
                Outcome.Error(e.message)
            }
        }
    }
}