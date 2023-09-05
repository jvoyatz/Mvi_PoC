package dev.jvoyatz.newarch.mvipoc.di

import android.util.Log
import androidx.lifecycle.ViewModel
import dev.jvoyatz.newarch.mvipoc.MainActivityContract
import dev.jvoyatz.newarch.mvipoc.MainViewModel
import timber.log.Timber

object ViewModelFactory {

    fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.d("create() called with: modelClass = " + modelClass)
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(AppFactory.getMoviesUseCase, MainActivityContract.MainViewState.Init) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}