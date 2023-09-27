package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppNavigator
import dev.jvoyatz.newarch.mvipoc.refactor.di.DataModule
import dev.jvoyatz.newarch.mvipoc.refactor.di.NavigationModule
import dev.jvoyatz.newarch.mvipoc.refactor.domain.repository.MoviesRepository
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.fakes.AppFakeNavigatorImpl
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.fakes.MoviesFakeRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NavigationModule::class, DataModule.DataModuleBinder::class]
)
abstract class FakeAppModule {

    @Binds
    @Singleton
    abstract fun bindNavigator(impl: AppFakeNavigatorImpl): AppNavigator

    @Singleton
    @Binds
    abstract fun bindsMoviesRepository(repositoryImpl: MoviesFakeRepository): MoviesRepository

}

