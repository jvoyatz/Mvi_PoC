package dev.jvoyatz.newarch.mvipoc.presentation.screen2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.jvoyatz.newarch.mvipoc.databinding.ActivityMainReduceBinding
import dev.jvoyatz.newarch.mvipoc.di.AppFactory
import dev.jvoyatz.newarch.mvipoc.di.ViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
 *         RocketsAvailableContent(
 *             snackbarHostState = snackbarHostState,
 *             uiState = uiState,
 *             onRocketClick = { onIntent(RocketClicked(it)) }
 *         )
 *     } else {
 *         RocketsNotAvailableContent(
 *             uiState = uiState
 *         )
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
 *         rocketList = uiState.rockets,
 *         onMovieClick = onMovieClick,
 *     )
 * }
 */
class MviReduceActivity : AppCompatActivity() {

    private val viewModel: MviReduceViewModel by lazy {
        ViewModelFactory.createV3(MviReduceViewModel::class.java)
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
        with(MoviesReduceAdapter {
            viewModel.postEvent(MviReduceContract.Event.OnMovieSelection)
        }) {
            binding.recyclerview.adapter = this
            showLoading()
        }
    }

    private fun setupObservers(binding: ActivityMainReduceBinding) {
        viewModel.state
            .onEach {
                handleMviReducedState(binding, it)
            }
            .launchIn(lifecycleScope)

        viewModel.effect().onEach {
            handleEffect(binding, it)
        }.launchIn(lifecycleScope)
    }

    private fun handleEffect(binding: ActivityMainReduceBinding, effect: MviReduceContract.Effect) {
        when (effect) {
            MviReduceContract.Effect.ShowError -> {
                Toast.makeText(this, "cannot show details for this movie", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleMviReducedState(
        binding: ActivityMainReduceBinding,
        state: MviReduceContract.MviReduceUiState
    ) {
        Timber.d("handleMviReducedState")
        if (state.isLoading) {
            renderLoadingState(binding)
        } else if (state.movies.isNotEmpty()) {
            renderResultsState(binding, state.movies)
        } else if (state.movies.isEmpty()) {
            renderNoResultsState(binding)
        }
    }

    private fun renderLoadingState(binding: ActivityMainReduceBinding) {
        with(binding) {
            noResults.visibility = View.GONE
            (recyclerview.adapter as MoviesReduceAdapter).showLoading()
        }
    }

    private fun renderNoResultsState(binding: ActivityMainReduceBinding) {
        with(binding) {
            noResults.visibility = View.VISIBLE
            recyclerview.visibility = View.GONE
        }
    }

    private fun renderResultsState(binding: ActivityMainReduceBinding, movies: List<MovieUiModel>) {
        with(binding) {
            noResults.visibility = View.GONE
            recyclerview.visibility = View.VISIBLE
            (recyclerview.adapter as MoviesReduceAdapter).apply {
                currentList.filter { movie ->
                    movie.id != MoviesReduceAdapter.TYPE_PROGRESS
                }.also {
                    submitList(it + movies)
                }
            }
        }
    }
}