package dev.jvoyatz.newarch.mvipoc.di

import android.content.Context
import dev.jvoyatz.newarch.mvipoc.data.MoviesRepository
import dev.jvoyatz.newarch.mvipoc.data.sources.local.MoviesDao
import dev.jvoyatz.newarch.mvipoc.data.sources.local.MoviesDatabase
import dev.jvoyatz.newarch.mvipoc.data.sources.remote.MoviesApiService
import dev.jvoyatz.newarch.mvipoc.domain.GetMoviesUseCase
import dev.jvoyatz.newarch.mvipoc.domain.GetMoviesUseCaseV3
import dev.jvoyatz.newarch.mvipoc.outcome.Outcome
import kotlinx.coroutines.flow.catch

object AppFactory {

    private val moviesApiService: MoviesApiService by lazy {
        MoviesApiService.getInstance()
    }

    private val moviesRepository by lazy {
        MoviesRepository(moviesApiService, moviesDao)
    }

    private lateinit var database: MoviesDatabase

    val moviesDao: MoviesDao
        get() = database.moviesDao()


    fun initDatabase(context: Context): MoviesDatabase {
        if(!::database.isInitialized){
            database = MoviesDatabase.getDatabase(context)
        }
        return database
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


    val getMoviesUseCaseV2 by lazy {
        GetMoviesUseCaseV3 {
            moviesRepository.getMoviesFlow(it)
                .catch {
                    //it.printStackTrace()
                    Outcome.Error(it.message)
                }
        }
    }
}