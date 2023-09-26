package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jvoyatz.newarch.mvipoc.refactor.core.presentation.BaseViewModel
import dev.jvoyatz.newarch.mvipoc.refactor.core.presentation.UiMutationPair
import dev.jvoyatz.newarch.mvipoc.refactor.core.presentation.mutation
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.OutcomeExtensions.onError
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.OutcomeExtensions.onSuccess
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.logThread
import dev.jvoyatz.newarch.mvipoc.refactor.domain.usecases.GetMoviesUseCase
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MovieListToDetailAction
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesAction
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesActionResult
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesActionResult.Loading
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesActionResult.Movies
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesActionResult.NoMovies
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesEvent
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesEvent.ErrorToast
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

const val MOVIES_SAVED_STATE_KEY = "moviesSavedStateKey!"

@HiltViewModel
class MovieListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val onMovieClicked: MovieListToDetailAction,
   // private val navigationCoordinator: NavigationCoordinator
) : BaseViewModel<MoviesState, MoviesAction, MoviesActionResult, MoviesEvent>(
    savedStateHandle,
    MOVIES_SAVED_STATE_KEY,
    MoviesState()
) {
    private var position: Int = 1

    //1st approach
    override fun mapAction(action: MoviesAction): Flow<UiMutationPair<MoviesActionResult?, MoviesEvent?>> = flow {
        println("callled!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        when(action) {
            MoviesAction.Initialize -> getMovies(position)
            is MoviesAction.GetMovies -> getMovies(++position)
            is MoviesAction.OnMovieSelection -> onMovieSelected(action.id).collect {
                emit(it)
            }
        }
    }
    //2nd approach
    fun mapAction2(action: MoviesAction): Flow<UiMutationPair<MoviesActionResult?, MoviesEvent?>> = when (action) {
        is MoviesAction.OnMovieSelection -> onMovieSelected(action.id)
        is MoviesAction.GetMovies -> getMovies2(++position)
        else -> emptyFlow()
    }
    private fun getMovies2(position: Int): Flow<UiMutationPair<MoviesActionResult, MoviesEvent>> {
        return flow {
            emit((Loading mutation null))
            getMoviesUseCase(position)
                .onSuccess {
                    val successResultHolder = if (it.isNullOrEmpty()) {
                        NoMovies mutation null
                    } else {
                        Movies(it) mutation null
                    }
                    emit(successResultHolder)
                }.onError {
                    emit(NoMovies mutation ErrorToast(it.message))
                }
        }
    }
    /**
     *      PLEASE NOTE THE DIFFERENCE
     *      both methods(this & above mapAction) return flow.
     *
     *      However, in the case of the above method we create a cold flow and we define our methods
     *      that handle the actions as extensions of FlowCollector.
     *
     *      In this (2nd) case, each method creates its own (cold) flow, and then is collected
     *      in the point where mapActions is invoked.
     *
     *
     *      fun mapAction2(action: MoviesAction): Flow<UiMutationPair<MoviesActionResult?, MoviesEvent?>> =
     *         when (action) {
     *             is MoviesAction.OnMovieSelection -> onMovieSelected(action.id)
     *             else -> emptyFlow<>()
     *         }
     */
    private suspend fun FlowCollector<UiMutationPair<MoviesActionResult?, MoviesEvent?>>.getMovies(
        position: Int
    ) {
        emit((Loading mutation null))



        getMoviesUseCase(position)
            .onSuccess {
                val successResultHolder = if (it.isNullOrEmpty()) {
                    NoMovies mutation null
                } else {
                    Movies(it) mutation null
                }
                emit(successResultHolder)
            }.onError {
                emit(NoMovies mutation ErrorToast(it.message))
            }
    }

    private fun onMovieSelected(id: Int): Flow<UiMutationPair<MoviesActionResult, MoviesEvent>> =
        flow {
            logThread()
            //1st way -- default way -- bad option
            //emit((null mutation OnMovieSelected(id)))
            //2nd way -- probably the best way, follows the coordinator pattern -- separation of concerns
            //clean approach
            onMovieClicked(id)
            //3rd way -- hybrid solution, however it works well
            //navigationCoordinator.navigate(AppDestination.NavGraphDestination.MovieDetails(id))
        }
}