package dev.jvoyatz.newarch.mvipoc.di

import android.util.Log
import androidx.lifecycle.ViewModel
import dev.jvoyatz.newarch.mvipoc.MainActivityContract
import dev.jvoyatz.newarch.mvipoc.MainViewModel
import dev.jvoyatz.newarch.mvipoc.MainViewModelV3
import timber.log.Timber

object ViewModelFactory {

    fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(AppFactory.getMoviesUseCase, MainActivityContract.MainViewState.Init) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

    fun <T : ViewModel> createV3(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModelV3::class.java)) {
            MainViewModelV3(AppFactory.getMoviesUseCaseV2) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}