package dev.jvoyatz.newarch.mvipoc.di

import androidx.lifecycle.ViewModel
import dev.jvoyatz.newarch.mvipoc.presentation.screen1.MainActivityContract
import dev.jvoyatz.newarch.mvipoc.presentation.screen1.MainViewModel
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.MviReduceViewModel
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.MviMoviesActionHandler
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract.MviMoviesReducer
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.example_viewmodels.MviReduceViewModelV4

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

    fun <T : ViewModel> createV4(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MviReduceViewModelV4::class.java)) {
            MviReduceViewModelV4(MviMoviesReducer(), MviMoviesActionHandler(AppFactory.getMoviesUseCaseV2, AppFactory.getMoviesUseCase)) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}