package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppNavigator
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MovieListRouter
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MovieListToDetailAction
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesState
import javax.inject.Named

const val ACTION_MOVIE_LIST_TO_DETAIL = "movieListToDetail"

@Module
@InstallIn(ViewModelComponent::class)
object MovieListBindsModule {

    @Provides
    @ViewModelScoped
    fun movieListRouter(appNavigator: AppNavigator): MovieListRouter = MovieListRouter(appNavigator)


    //////////////////////////////////////////////////////////////////////////////////////////////////
    //either this
    @Provides
    fun provideMoviesListToDetailNavAction(router: MovieListRouter): MovieListToDetailAction {
        return MovieListToDetailAction.create(router)
    }

    // leaving this here for education purposes
    //        @Provides
    //        @Named("ACTION_MOVIE_LIST_TO_DETAIL")
    //        fun provideMoviesListToDetailNavAction2(router: MovieListRouter): (Long) -> Unit {
    //            return MovieListToDetailAction.create(router)
    //        }

    @Provides
    @Named("ACTION_MOVIE_LIST_TO_DETAIL")
    fun provideMoviesListToDetailNavActionNamed(router: MovieListRouter): (Int) -> Unit = {
        router.navigateToMovieDetails(it)
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
}

