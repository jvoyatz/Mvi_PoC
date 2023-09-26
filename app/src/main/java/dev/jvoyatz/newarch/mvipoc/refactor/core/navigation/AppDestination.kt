package dev.jvoyatz.newarch.mvipoc.refactor.core.navigation


/**
 * Navigation Destinations are defined here
 * A destination can be a fragment inside Navigation's Component nav graph
 * or another activity/fragment that is not included inside a nav graph and
 * we need to open this screen manually
 */
sealed interface AppDestination {
    sealed interface ExplicitDestination : AppDestination {
       // fun add(block: (Fragment) -> Unit)
        data object ManualNavDestination1 : ExplicitDestination
        data object ManualNavDestination2 : ExplicitDestination
    }
    sealed interface NavGraphDestination: AppDestination {
     //  data object Movies : NavComponentDestination
        data class MovieDetails(val id: Int) : NavGraphDestination

        data object Back: AppDestination
    }

}