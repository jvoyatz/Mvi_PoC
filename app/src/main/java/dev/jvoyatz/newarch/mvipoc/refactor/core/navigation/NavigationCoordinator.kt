package dev.jvoyatz.newarch.mvipoc.refactor.core.navigation

import dev.jvoyatz.newarch.mvipoc.refactor.di.ImmediateScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationCoordinator @Inject constructor(
    @ImmediateScope private val scope: CoroutineScope
) {

    private val channel = Channel<AppDestination>()
    val destinationFlow = channel.receiveAsFlow()

    fun navigate(destination: AppDestination){
        scope.launch {
            Timber.d("launch $destination")
            channel.send(destination)
        }
    }
}