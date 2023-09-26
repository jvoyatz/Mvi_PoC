package dev.jvoyatz.newarch.mvipoc.refactor.core.presentation


/**
 * Base contract for actions (meaning actions taken by the User)
 */
interface UiAction //getmovies

/**
 * Each [UiAction] comes with a change/mutation which is represented
 * by the following interface.
 *
 * While a [UiAction] is in progress or it has finished, specific changes
 * are about to be applied in the (single) Ui State that our view renders.
 */
interface UiMutation<T: UiState>{
    fun reduce(state: T): T
}


/**
 * Simple holder class that can contain two nullable fields --
 * acts like the predefined Kotlin's class [Pair].
 */
data class UiMutationPair<out A, out B>(
    val mutation: A?, val event: B?

) {
    override fun toString(): String {
        return "UiMutationPair [result=$mutation, event=$event]"
    }

    companion object {
        //fun factory..
    }
}
//infix fun <A, B> Nothing?.toMutation(event: B) = UiMutationPair<A, B>(null, event)
infix fun <A: UiMutation<*>, B: UiEvent> A?.mutation(event: B?): UiMutationPair<A, B> = UiMutationPair(this, event)
//fun <A: UiMutation<*>, B: UiEvent> B.event(): UiMutationPair<A, B> = UiMutationPair(null, this)
//fun <A: UiMutation<*>, B: UiEvent> A.mutation(): UiMutationPair<A, B> = UiMutationPair(this, null)
