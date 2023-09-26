package dev.jvoyatz.newarch.mvipoc.refactor.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.AppDispatchers
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

const val SCOPE_MAIN_IMMEDIATE = "MAIN_IMMEDIATE_SCOPE"

@Retention
@Qualifier
annotation class ImmediateDispatcher

@Retention
@Qualifier
annotation class ImmediateScope

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {


    // read this
    // https://github.com/Kotlin/kotlinx.coroutines/issues/2886#issuecomment-901188295
    @ImmediateDispatcher
    @Singleton
    @Provides
    fun provideDispatcherImmediate(): CoroutineDispatcher = Dispatchers.Main.immediate


    @ImmediateScope
    @Singleton
    @Provides
    fun provideImmediateScope(@ImmediateDispatcher dispatcher: CoroutineDispatcher): CoroutineScope{
        val exceptionHandler = CoroutineExceptionHandler{ _, throwable -> throwable.printStackTrace() }
        return CoroutineScope( SupervisorJob() + exceptionHandler +  dispatcher)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class AppBinderModule {
        @Singleton
        @Binds
        abstract fun bindsDispatcherProvider(provider: AppDispatchers): DispatcherProvider
    }
}