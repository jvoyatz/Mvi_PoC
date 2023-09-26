package dev.jvoyatz.newarch.mvipoc.refactor.core.navigation;

/**
 * My references for navigation
 *  1. https://proandroiddev.com/how-to-make-jetpack-compose-navigation-easier-and-testable-b4b19fd5f2e4
 *  2. https://medium.com/bumble-tech/scalable-jetpack-compose-navigation-9c0659f7c912
 *
 *
 *
 *  Jetpack Navigation:
 *      https://proandroiddev.com/how-to-make-jetpack-compose-navigation-easier-and-testable-b4b19fd5f2e4
 *      https://developer.android.com/jetpack/compose/navigation#interoperability
 *
 *
 * Explanation of my approach-->
 *
 * Where should we place app navigation logic?
 *
 * First things first: https://martinfowler.com/books/eaa.html
 *
 * We don't want our views to be coupled with navigation logic. You might want to reuse a specific
 * view in the future, you should respect the separation of concerns principle.
 *
 * We said (above), that we don't want to put navigation logic in the view, and for we won't do this in business logic (use cases or repository).
 * That means, there is one place to place navigation logic and it is called Viewmodel.
 *
 * However, even if we inject our Navigator in the Viewmodel, does this class (ViewModel) need to know about where
 * we can navigate using our navigator? Of course, no, it shouldn't have so many responsibilities.
 * That's why we introduce a new Level ob abstraction which is responsible for navigating us to a destination
 * as a result of a certain action.
 *
 */