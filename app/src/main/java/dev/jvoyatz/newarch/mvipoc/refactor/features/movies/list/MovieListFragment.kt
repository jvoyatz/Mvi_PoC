package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.jvoyatz.newarch.mvipoc.databinding.FragmentMovieListBinding
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesAction
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesEvent
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesState
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private var toast: Toast ? = null
    private lateinit var binding: FragmentMovieListBinding

     private val viewModel: MovieListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = (FragmentMovieListBinding.inflate(inflater, container, false)).run {
        binding = this
        this.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView(){
        val loadMore = {
            viewModel.onAction(MoviesAction.GetMovies)
        }

        with(MoviesAdapter(loadMore){
            viewModel.onAction(MoviesAction.OnMovieSelection(it))
        }){
            binding.recyclerview.adapter = this
        }
    }

    private fun setupObservers(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { onMovieUiStateEmission(it) }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect {
                    handleEvent(it)
                }
            }
        }
    }


    private fun handleEvent(event: MoviesEvent) {
        when(event){
            is MoviesEvent.ErrorToast -> {
                toast?.cancel()
                toast = Toast.makeText(requireContext(), event.messsage, Toast.LENGTH_LONG)
                toast?.show()
            }
            is MoviesEvent.OnMovieSelected -> {
                Timber.d("we caught the emission of onMovieSelection event... however we don't want our screen" +
                        "to take a decision regarding navigation.. that means that this log is printed here for educational purposes")
            }
        }
    }

    private fun onMovieUiStateEmission(state: MoviesState){
        Timber.d("onMovieUiStateEmission() called with: state = $state")
        when{
            state.isLoading -> renderLoadingState()
            state.error != null -> TODO()
            state.movies != null -> renderContentState(state.movies)
            else -> viewModel.onAction(MoviesAction.Initialize)
        }
    }

    private fun renderLoadingState() {
        with(binding){
            noResults.visibility = View.GONE
            getAdapter().showLoading()
        }
    }

    private fun renderContentState(movies: List<Movie>) {
        with(binding) {
            if (movies.isNotEmpty()) {
                noResults.visibility = View.GONE
                recyclerview.visibility = View.VISIBLE
                getAdapter().apply {
                    currentList.filter { movie ->
                        movie.id != MoviesAdapter.TYPE_PROGRESS
                    }.also {
                        submitList(it + movies)
                    }
                }
            } else {
                noResults.visibility = View.VISIBLE
                recyclerview.visibility = View.GONE
            }
        }
    }

    private fun getAdapter(): MoviesAdapter {
        return binding.recyclerview.adapter as MoviesAdapter
    }
}