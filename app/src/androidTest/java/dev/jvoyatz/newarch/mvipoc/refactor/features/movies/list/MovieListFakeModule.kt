package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppDestination
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppNavigator
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppNavigatorImpl
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.Outcome
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie
import dev.jvoyatz.newarch.mvipoc.refactor.domain.repository.MoviesRepository
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MovieListRouter
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MovieListToDetailAction
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.di.MovieListBindsModule
import javax.inject.Named

const val ACTION_MOVIE_LIST_TO_DETAIL = "movieListToDetail"

//@Module
//@TestInstallIn(
//    components = [ViewModelComponent::class],
//    replaces = [MovieListBindsModule::class]
//)
//object MovieListFakeModule {
//    @Provides
//    @ViewModelScoped
//    fun movieListRouter(appNavigator: AppNavigator): MovieListRouter = MovieListRouter(appNavigator)
//
//
//    //////////////////////////////////////////////////////////////////////////////////////////////////
//    //either this
//    @Provides
//    fun provideMoviesListToDetailNavAction(router: MovieListRouter): MovieListToDetailAction {
//        return MovieListToDetailAction.create(router)
//    }
//    @Provides
//    @Named("ACTION_MOVIE_LIST_TO_DETAIL")
//    fun provideMoviesListToDetailNavActionNamed(router: MovieListRouter): (Int) -> Unit = {
//        router.navigateToMovieDetails(it)
//    }
//}
//
