package dev.jvoyatz.newarch.mvipoc.refactor.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppNavigator
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppNavigatorImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigator(impl: AppNavigatorImpl): AppNavigator

}

