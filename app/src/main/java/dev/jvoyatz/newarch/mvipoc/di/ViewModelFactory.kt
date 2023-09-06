package dev.jvoyatz.newarch.mvipoc.di

import androidx.lifecycle.ViewModel
import dev.jvoyatz.newarch.mvipoc.presentation.screen1.MainActivityContract
import dev.jvoyatz.newarch.mvipoc.presentation.screen1.MainViewModel
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.MviReduceViewModel

object ViewModelFactory {

    fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(AppFactory.getMoviesUseCase, MainActivityContract.MainViewState.Init) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

    fun <T : ViewModel> createV3(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MviReduceViewModel::class.java)) {
            MviReduceViewModel(AppFactory.getMoviesUseCaseV2) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}