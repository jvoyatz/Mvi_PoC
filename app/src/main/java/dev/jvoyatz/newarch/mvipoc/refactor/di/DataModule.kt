package dev.jvoyatz.newarch.mvipoc.refactor.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jvoyatz.newarch.mvipoc.refactor.data.MoviesRepositoryImpl
import dev.jvoyatz.newarch.mvipoc.refactor.data.remote.MoviesApi
import dev.jvoyatz.newarch.mvipoc.refactor.domain.repository.MoviesRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideMoviesApi(): MoviesApi = MoviesApi.getInstance()

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class DataModuleBinder {
        @Singleton
        @Binds
        abstract fun bindsMoviesRepository(repositoryImpl: MoviesRepositoryImpl): MoviesRepository
    }
}