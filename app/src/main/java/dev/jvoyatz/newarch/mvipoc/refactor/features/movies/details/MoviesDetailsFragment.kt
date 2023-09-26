package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.jvoyatz.newarch.mvipoc.databinding.FragmentMovieDetailsBinding
import timber.log.Timber


@AndroidEntryPoint
class MoviesDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
    }
}