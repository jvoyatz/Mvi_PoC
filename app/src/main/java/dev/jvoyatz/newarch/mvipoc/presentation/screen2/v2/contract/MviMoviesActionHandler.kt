package dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.contract

import dev.jvoyatz.newarch.mvipoc.domain.GetMoviesUseCase
import dev.jvoyatz.newarch.mvipoc.domain.GetMoviesUseCaseV3
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.onError
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.onSuccess
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.base.ActionHandler
import dev.jvoyatz.newarch.mvipoc.presentation.screen2.v2.base.ActionResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.lang.Exception

class MviMoviesActionHandler(
    private val getMoviesUseCaseFlow: GetMoviesUseCaseV3,
    private val getMoviesUseCase: GetMoviesUseCase,
): ActionHandler<UiAction, MoviesPartialStateV2, UiEffectV2> {
    override fun invoke(action: UiAction): Flow<ActionResult<MoviesPartialStateV2?, UiEffectV2?>> {
        Timber.d("invoke() called with: action = " + action)
//        return when(action){
//                is UiAction.GetMovies -> getMovies(action.page)
//                UiAction.OnMovieSelection -> TODO()
//        }


        return flow {
            when(action){
                is UiAction.GetMovies -> getMovies2(action.page)
                UiAction.OnMovieSelection -> emit(ActionResult(MoviesPartialStateV2.ShowError("REPEaTABLE ERROR"), UiEffectV2.ShowErrorToast("cannot get movies details atm")))
            }
        }
    }

    //or this
    private suspend fun FlowCollector<ActionResult<MoviesPartialStateV2, UiEffectV2>>.getMovies2(position:Int) {
        emit(ActionResult(MoviesPartialStateV2.ShowLoading, null))

        delay(1000)
        try{
            getMoviesUseCase(position)
            .onSuccess {
                Timber.w("onsuccess called")
                kotlin.run {
                    if (it.isNullOrEmpty()) {
                        ActionResult(MoviesPartialStateV2.ShowEmpty, null)
                    } else {
                        ActionResult(MoviesPartialStateV2.ShowContent(it), null)
                    }
                }.also {
                    emit(it)
                }
            }.onError {
                Timber.w("onError called, emitting error")
                emit(ActionResult(null, UiEffectV2.ShowErrorToast("error message, ignore for now")))
            }
        }catch (e: Exception){
            emit(ActionResult(null, UiEffectV2.ShowErrorToast(e.message ?: "unknown error")))
        }
    }
}

////
////        delay(5000)
////        getMovies(position).collect{
////            Timber.d("collecting new state $it")
////            emit(it)
////        }

//this
//    private fun getMovies(position: Int): Flow<ActionResult<PartialStateV2, UiEffectV2.ShowErrorToast>> {
//        Timber.d("getMovies() called with: position = " + position)
//        return getMoviesUseCaseFlow(position)
//            .map {
//
//                if (it is Outcome.Success && it.value != null) {
//                    ActionResult(PartialStateV2.ShowContent(it.value), null)
//                } else if (it is Outcome.Success) {
//                    ActionResult(PartialStateV2.ShowEmpty, null)
//                } else {
//                    ActionResult(null, UiEffectV2.ShowErrorToast("error message, ignore"))
//                }
//            }
//            .onStart {
//                emit(ActionResult(PartialStateV2.ShowLoading, null))
//            }
//    }
