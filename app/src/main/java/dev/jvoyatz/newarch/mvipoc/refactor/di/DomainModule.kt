package dev.jvoyatz.newarch.mvipoc.refactor.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jvoyatz.newarch.mvipoc.refactor.domain.repository.MoviesRepository
import dev.jvoyatz.newarch.mvipoc.refactor.domain.usecases.GetMoviesUseCase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideGetMoviesUseCase(repository: MoviesRepository): GetMoviesUseCase =  GetMoviesUseCase.create(repository)
}


