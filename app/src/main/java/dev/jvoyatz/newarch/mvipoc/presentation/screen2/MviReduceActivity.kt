package dev.jvoyatz.newarch.mvipoc.presentation.screen2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.jvoyatz.newarch.mvipoc.databinding.ActivityMainReduceBinding
import dev.jvoyatz.newarch.mvipoc.di.AppFactory
import dev.jvoyatz.newarch.mvipoc.di.ViewModelFactory
import dev.jvoyatz.newarch.mvipoc.domain.Movie
import dev.jvoyatz.newarch.mvipoc.presentation.screen1.MoviesAdapter
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.UiAction
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.UiEffectV2
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.UiStateV2
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.example_viewmodels.MviReduceViewModelV4
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * @Composable
 * fun MyMovies(
 *     viewModel: MviReduceViewModel = hiltViewModel() //or other injection
 * ) {
 *
 *     val uiState by viewModel.uiState.collectAsStateWithLifecycle()
 *
 *     MoviesScreen(
 *         uiState = uiState,
 *         (...)
 *     )
 * }
 *
 * @Composable
 * internal fun MoviesScreen(
 *     uiState: MviState,
 *     onEvent: (MviReduceContract.Event) -> Unit
 * ) {
 *     if (uiState.movies.isNotEmpty()) {
 *         MoviesContent(
 *             snackbarHostState = snackbarHostState,
 *             uiState = uiState,
 *             onMovieClick = { onIntent(MovieClicked(it)) }
 *         )
 *     } else {
 *         //.....
 *     }
 * }
 *
 * @Composable
 * private fun MoviesContent(
 *     uiState: MviState,
 *     onMovieClick: (Int) -> Unit,
 * ) {
 *     if (uiState.isError) {
 *         //show dialog
 *     }
 *
 *     MoviesList(
 *         moviesList = uiState.movies,
 *         onMovieClick = onMovieClick,
 *     )
 * }
 */
@AndroidEntryPoint
class MviReduceActivity : AppCompatActivity() {

    private val viewModel: MviReduceViewModelV4 by lazy {
        ViewModelFactory.createV4(MviReduceViewModelV4::class.java)
    }

    private var currentPosition = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        AppFactory.initDatabase(this.applicationContext)

        with(ActivityMainReduceBinding.inflate(layoutInflater)) {
            setContentView(root)
            setupRecyclerView(this)
            setupObservers(this)
        }
    }

    private fun setupRecyclerView(binding: ActivityMainReduceBinding) {
//        with(MoviesReduceAdapter {
//            viewModel.postEvent(MviReduceContract.Event.OnMovieSelection)
//        }) {
//            binding.recyclerview.adapter = this
//            showLoading()
//        }

        with(MoviesAdapter {
            viewModel.onAction(UiAction.OnMovieSelection)
        }) {
            binding.recyclerview.adapter = this
            //showLoading()
        }
    }

    private fun setupObservers(binding: ActivityMainReduceBinding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect {
                    Timber.e("new state emission $it")
                    handleMviReducedState(binding, it)
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.effectFlow.collectLatest {
                    Timber.d("collecting effect!")
                    handleEffect(binding, it)
                }
            }
        }
    }

    private fun handleEffect(binding: ActivityMainReduceBinding, effect: UiEffectV2) {
        when (effect) {
            is UiEffectV2.ShowErrorToast -> {
                Toast.makeText(this, "cannot show details for this movie", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleMviReducedState(
        binding: ActivityMainReduceBinding,
        state: UiStateV2
    ) {
        Timber.d("handleMviReducedState $state")
        if(state.isIdle) {
            //trigger an action here??
        } else if (state.isLoading) {
            Timber.d("show loading?")
            renderLoadingState(binding)
        } else if (state.hasMovies && state.movies.isNotEmpty()) {
            renderResultsState(binding, state.movies)
        } else if (state.hasMovies && state.movies.isEmpty()) {
            renderNoResultsState(binding)
        } else if(state.hasError){
            Toast.makeText(this, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!cannot show details for this movie", Toast.LENGTH_LONG).show()
            //....
            //consumed error, update state
        }
    }

    private fun renderLoadingState(binding: ActivityMainReduceBinding) {
        with(binding) {
            noResults.visibility = View.GONE
            recyclerview.visibility = View.VISIBLE
            (recyclerview.adapter as MoviesAdapter).showLoading()
        }
    }

    private fun renderNoResultsState(binding: ActivityMainReduceBinding) {
        with(binding) {
            noResults.visibility = View.VISIBLE
            recyclerview.visibility = View.GONE
        }
    }

    private fun renderResultsState(binding: ActivityMainReduceBinding, movies: List<Movie>) {
        with(binding) {
            noResults.visibility = View.GONE
            recyclerview.visibility = View.VISIBLE
            (recyclerview.adapter as MoviesAdapter).apply {
                currentList.filter { movie ->
                    movie.id != MoviesAdapter.TYPE_PROGRESS
                }.also {
                    submitList(it + movies)
                }
            }
        }
    }
}