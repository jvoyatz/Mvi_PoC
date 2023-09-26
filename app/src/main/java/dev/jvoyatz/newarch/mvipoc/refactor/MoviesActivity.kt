package dev.jvoyatz.newarch.mvipoc.refactor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.jvoyatz.newarch.mvipoc.R
import dev.jvoyatz.newarch.mvipoc.databinding.ActivityMoviesBinding
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppDestination
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppNavigator
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.NavigationCoordinator
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.MovieListFragmentDirections
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {
    //approach 1
    @Inject
    lateinit var navigator: AppNavigator

    //approach 2
    @Inject
    lateinit var navigationCoordinator: NavigationCoordinator
    private val navController by lazy {
        NavHostFragment.findNavController(supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(ActivityMoviesBinding.inflate(layoutInflater)){
            setContentView(root)
        }

        setup()
    }
    override fun onDestroy() {
        super.onDestroy()
        navigator.clear()
    }


    private fun setup() {
        //this
        navigator.init {
            NavHostFragment.findNavController(supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!) to supportFragmentManager
        }

        //or this
        lifecycleScope.launch {
            navigationCoordinator.destinationFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    onNavEvent(it)
                }
        }
    }
    private fun onNavEvent(destination: AppDestination){
        Timber.d("onNavEvent() called with: destination = $destination")
        when (destination) {
            is AppDestination.NavGraphDestination.MovieDetails -> navController.navigate(
                MovieListFragmentDirections.actionMoviesListFragmentToMoviesDetailsFragment()
            )

            AppDestination.ExplicitDestination.ManualNavDestination1 -> TODO()
            AppDestination.ExplicitDestination.ManualNavDestination2 -> TODO()
            AppDestination.NavGraphDestination.Back -> navController.popBackStack()
        }
    }
}