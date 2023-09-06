package dev.jvoyatz.newarch.mvipoc.presentation.screen1


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.MviReduceViewModel
import dev.jvoyatz.newarch.mvipoc.databinding.ActivityMainBinding
import dev.jvoyatz.newarch.mvipoc.di.AppFactory
import dev.jvoyatz.newarch.mvipoc.di.ViewModelFactory
import dev.jvoyatz.newarch.mvipoc.domain.Movie
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelFactory.create(MainViewModel::class.java)
    }

    private var currentPosition = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        AppFactory.initDatabase(this)

        with(ActivityMainBinding.inflate(layoutInflater)) {
            setContentView(root)
            setupRecyclerView(this)
            setupObservers(this)
        }
    }


    private fun setupRecyclerView(binding: ActivityMainBinding){
        with(MoviesAdapter {
            viewModel.postEvent(MainActivityContract.Event.GetMovies(++currentPosition))
        }){
            binding.recyclerview.adapter = this
            showLoading()
        }
    }

    private fun setupObservers(binding: ActivityMainBinding){
//        viewModel.state()
//            .map {
//                it.mainViewState
//            }.onEach {
//                handleMainViewState(binding, it)
//            }
//            .launchIn(lifecycleScope)
//
//
//        viewModel.effect().onEach {
//            handleEffect(binding, it)
//        }.launchIn(lifecycleScope)
        setupObserversV3(binding)
    }

    private fun setupObserversV3(binding: ActivityMainBinding){
        viewModel.state()
            .map {
                it.mainViewState
            }
           .onEach {
                handleMainViewState(binding, it)
            }
            .launchIn(lifecycleScope)


        viewModel.effect().onEach {
            handleEffect(binding, it)
        }.launchIn(lifecycleScope)
    }

    private fun handleEffect(binding: ActivityMainBinding, effect: MainActivityContract.Effect) {
        when(effect){
            MainActivityContract.Effect.ShowError -> {
                Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show()
                --currentPosition
                (binding.recyclerview.adapter as MoviesAdapter).apply {
                    currentList.filter { movie ->
                        movie.id != MoviesAdapter.TYPE_PROGRESS
                    }.also {
                        submitList(it)
                    }
                }
            }
        }
    }

    private fun handleMainViewState(binding: ActivityMainBinding, state: MainActivityContract.MainViewState){
        Timber.d("handleMainViewState() called with: binding = " + binding + ", state = " + state)
        when(state){
            MainActivityContract.MainViewState.Init -> viewModel.postEvent(MainActivityContract.Event.Init)
            MainActivityContract.MainViewState.Loading -> renderLoadingState(binding)
            MainActivityContract.MainViewState.NoResults -> renderNoResultsState(binding)
            is MainActivityContract.MainViewState.Results -> renderResultsState(binding, state.movies)
        }
    }

    private fun renderLoadingState(binding: ActivityMainBinding) {
        with(binding){
            noResults.visibility = View.GONE
            (recyclerview.adapter as MoviesAdapter).showLoading()
        }
    }

    private fun renderNoResultsState(binding: ActivityMainBinding){
        with(binding){
            noResults.visibility = View.VISIBLE
            recyclerview.visibility = View.GONE
        }
    }
    private fun renderResultsState(binding: ActivityMainBinding, movies: List<Movie>){
        with(binding){

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