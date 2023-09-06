

# What should i use?

**I think that in order to be able to work in a better way, you need to discuss with
the rest of the team to find or build custom solutions and use them again & again. Maybe one day, you
wake up and think that it was a bad idea and come with a better solution**

# Notes

* god activity -> mvp -> mvvm -> mvi
  * event buses
  * rxjava
  * clean architecture
* android dev constantly evolves
  * hard to follow
* viewmodel ? 
  * a new version that replaced the Loaders
  * a retained object, stored in a Map which does not survive process death
    * for this you need the SavedStateHandle
    * acts a presenter

## MVI

Has a lot in common with MVVM, however is a more structured architecture and provides a better 
state management. <br/>

#### Basic Info

  * defines a set of events/intents/actions, which are handled by the viewmodel
  * diff from mvvm?
    * mvvm holds a separate data holder for each piece of data
    * mvi gives us the capability to have a single object for handling the state of the view
  * in compare with mvp, mvi(and mvvm), it abstracts away the logic from the UI
  * separates every step, so testing is easy --> predictable
  * it is consisted of three essential parts 
    * **Model**, represents the state of the ui
      * it is not necessary to used  sealed classes for the UiState, you can just use a data class with variables
    * **View**,   observes user's actions and reacts
    * **Intent**, a user's actions which changes the state of the model
  * acts as a state machine
    * only one state is present/active at a time
  * Redux -> Reducer(has an initial state, some info about what to do with it(action) and then produces a new state)
    * but can things be simpler? why having a **reducer** to act as mediator
  * need to distinguish content from error (use of another class to handle the effects)
    * why? because in case of an orientation change (or a process death) you will see a again an error dialog/toast
  * imagines user as a function
    * creates events
    * processed
    * rendered


#### Advantages
* single source of truth -- state is defined by data set in one class
* following/reading the code is easier because the flow of data is unidirectional
* testing
* Jetpack compose
* makes it easier to locate bugs, find the solution for each one and fixing them
* everything is in an once place -> better understanding
* immutable ----> threadsafe

#### Disadvantages
* boilerplate
* maybe overkill
* learning curve
* memory management
* try not to make the state classes huge





