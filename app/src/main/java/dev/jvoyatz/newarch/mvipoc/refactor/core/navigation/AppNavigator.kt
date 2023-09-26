package dev.jvoyatz.newarch.mvipoc.refactor.core.navigation

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController

/**
 * We could use an implementation without this interface, however
 * we prefer to use this instead of a concrete type see [AppNavigatorImpl],
 * in order to provide a fake or a mocked version of this class in case we want to test
 * our classes.
 */
interface AppNavigator/*<T: Destination>*/{
    fun navigate(destination: AppDestination)
    fun init(block: () -> Pair<NavController, FragmentManager>)
    fun clear()
}