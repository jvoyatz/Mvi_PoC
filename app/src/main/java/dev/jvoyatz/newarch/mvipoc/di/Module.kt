package dev.jvoyatz.newarch.mvipoc.di

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jvoyatz.newarch.mvipoc.R

//@InstallIn(SingletonComponent::class)
//@Module
object DaggerModule {
   // @Provides
    fun providesNavController(activity: FragmentActivity): NavController {
        return NavHostFragment.findNavController(activity.supportFragmentManager.findFragmentById(
            androidx.navigation.fragment.R.id.nav_host_fragment_container)!!)
    }
}