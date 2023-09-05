package dev.jvoyatz.newarch.mvipoc.outcome

import retrofit2.Response

sealed interface Outcome<out T> {
    class Success<T>(val value: T?) : Outcome<T>
    data class Error(val message: String? = null) : Outcome<Nothing>

}


object OutcomeExtensions {
    inline fun <T> Outcome<T>.onSuccess(action: (T?) -> Unit): Outcome<T> = when (this) {
        is Outcome.Success -> apply { action(value) }
        is Outcome.Error -> this
    }

    inline fun <T> Outcome<T>.onError(action: (Outcome.Error) -> Unit): Outcome<T> = when (this) {
        is Outcome.Success -> this
        is Outcome.Error -> apply { action(this) }
    }

    inline fun <T, R> Outcome<T>.mapSuccess(mapper: T.() -> R): Outcome<R> {
        return when (this) {
            is Outcome.Success -> value?.mapper().toSuccessfulOutcome()
            is Outcome.Error -> this
        }
    }

    inline fun <T> outcomeFromApiCall(block: () -> Response<T>): Outcome<T> {
        val result = block()

        return if (result.isSuccessful) result.body().toSuccessfulOutcome()
        else Outcome.Error(result.message())

    }

    fun <T> T?.toSuccessfulOutcome() = Outcome.Success(this)
}

