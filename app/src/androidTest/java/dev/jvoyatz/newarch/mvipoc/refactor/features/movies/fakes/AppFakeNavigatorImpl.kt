package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.fakes

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppDestination
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.AppNavigator
import javax.inject.Inject

class AppFakeNavigatorImpl @Inject constructor(): AppNavigator {
    override fun navigate(destination: AppDestination) {
        TODO("Not yet implemented")
    }

    override fun init(block: () -> Pair<NavController, FragmentManager>) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }
}