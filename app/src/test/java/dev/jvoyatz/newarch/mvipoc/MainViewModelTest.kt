package dev.jvoyatz.newarch.mvipoc

import app.cash.turbine.test
import com.google.common.truth.Truth
import dev.jvoyatz.newarch.mvipoc.TestUtils.runWithTestDispatcher
import dev.jvoyatz.newarch.mvipoc.domain.GetMoviesUseCase
import dev.jvoyatz.newarch.mvipoc.outcome.Outcome
import dev.jvoyatz.newarch.mvipoc.outcome.OutcomeExtensions.toSuccessfulOutcome
import dev.jvoyatz.newarch.mvipoc.domain.Movie
import dev.jvoyatz.newarch.mvipoc.presentation.screen1.MainActivityContract
import dev.jvoyatz.newarch.mvipoc.presentation.screen1.MainViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val rule = TestDispatcherRule()


    private val getMoviesUseCaseMock: GetMoviesUseCase = mockk()
    private val initMainViewState = MainActivityContract.MainViewState.Init

    //subject under test
    private lateinit var sut: MainViewModel

    @Before
    fun setup() {
        sut = MainViewModel(
            getMoviesUseCaseMock,
            initMainViewState
        )
    }

    @Test
    fun `in case of the first time, state is init`() = runTest {
        //given
        //no setup needed

        //when
        sut.state().test {
            val emittedState = awaitItem().mainViewState

            //then
            Truth.assertThat(emittedState).isEqualTo(MainActivityContract.MainViewState.Init)
        }
    }


    /**
     * class UseCase{
     *      override fun operator invoke() {
     *
     *      }
     *
     *      fun execute(){
     *
     *      }
     * }
     *
     * val useCase = UseCase()
     * useCase.invoke()
     * useCase()
     */
    @Test
    fun `when handling init event then get movies`() = runWithTestDispatcher {
        //given
        //no setup needed
        val event = MainActivityContract.Event.Init
        coEvery { getMoviesUseCaseMock.invoke(any())} returns Outcome.Success(listOf())

        //when
        sut.postEvent(event)

        //then
        coVerify { getMoviesUseCaseMock(1) }
    }

    @Test
    fun `when handling get movies event then get movies`() = runTest {
        //given
        //no setup needed
        val event = MainActivityContract.Event.GetMovies(1)
        coEvery { getMoviesUseCaseMock(any()) } returns Outcome.Success(listOf())

        //when
        sut.postEvent(event)

        //then
        coVerify { getMoviesUseCaseMock(1) }
    }

    @Test
    fun `when getting movies then state is set to loading`() = runTest {
        //given
        //no setup needed
        val event = MainActivityContract.Event.GetMovies(1)
        coEvery { getMoviesUseCaseMock(any()) } returns null.toSuccessfulOutcome()

        //when
        sut.state().test {
            // is not an actual UIState
            sut.postEvent(event)
            skipItems(1)
            val emittedState = awaitItem().mainViewState
            skipItems(1)

            Truth.assertThat(emittedState).isEqualTo(MainActivityContract.MainViewState.Loading)
        }
    }

    @Test
    fun `when getting movies then state is set to results`() = runTest {
        //given
        //no setup needed
        val event = MainActivityContract.Event.GetMovies(1)
        val results = listOf<Movie>()
        coEvery { getMoviesUseCaseMock(any()) } returns results.toSuccessfulOutcome()

        //when
        sut.state().test {
            sut.postEvent(event)
            skipItems(2)
            val emittedState = awaitItem().mainViewState
            Truth.assertThat(emittedState)
                .isEqualTo(MainActivityContract.MainViewState.Results(results))
        }
    }

    @Test
    fun `when getting movies returns null then state is set to no results`() = runTest {
        //given
        //no setup needed
        val event = MainActivityContract.Event.GetMovies(1)
        val results = null
        coEvery { getMoviesUseCaseMock(any()) } returns results.toSuccessfulOutcome()

        //when
        sut.state().test {
            sut.postEvent(event)
            skipItems(2)
            val emittedState = awaitItem().mainViewState
            Truth.assertThat(emittedState).isEqualTo(MainActivityContract.MainViewState.NoResults)
        }
    }

    @Test
    fun `when getting movies returns error then effect is set to show toast`() = runTest {
        //given
        //no setup needed
        val event = MainActivityContract.Event.GetMovies(1)

        coEvery { getMoviesUseCaseMock(any()) } returns Outcome.Error("mock error")

        //when
        sut.effect().test {
            sut.postEvent(event)
            val emittedEffect = awaitItem()
            //then
            Truth.assertThat(emittedEffect).isEqualTo(MainActivityContract.Effect.ShowError)
        }
    }
}