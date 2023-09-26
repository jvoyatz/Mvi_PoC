@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth
import dev.jvoyatz.newarch.mvipoc.TestDispatcherRule
import dev.jvoyatz.newarch.mvipoc.TestUtils
import dev.jvoyatz.newarch.mvipoc.TestUtils.runWithTestDispatcher
import dev.jvoyatz.newarch.mvipoc.fakes.FakeData
import dev.jvoyatz.newarch.mvipoc.fakes.FakeData.noMoviesState
import dev.jvoyatz.newarch.mvipoc.fakes.FakeGetMoviesUseCase
import dev.jvoyatz.newarch.mvipoc.fakes.FakeMovieListToDetailAction
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.NavigationCoordinator
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.Outcome
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.OutcomeExtensions.toSuccessfulOutcome
import dev.jvoyatz.newarch.mvipoc.refactor.data.MoviesMappers.dtoToMovies
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie
import dev.jvoyatz.newarch.mvipoc.refactor.domain.usecases.GetMoviesUseCase
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MovieListToDetailAction
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesAction
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesEvent
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesState
import io.mockk.MockK
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTestMock{

    private val getMoviesUseCaseMock: GetMoviesUseCase = mockk()
    private val listToDetailAction: MovieListToDetailAction = mockk()

    private lateinit var sut: MovieListViewModel

    @Before
    fun setup(){
        val savedStateHandle = SavedStateHandle(mapOf("test" to 1))
        sut = MovieListViewModel(
            savedStateHandle,
            getMoviesUseCaseMock,
            listToDetailAction
        )
    }

    @Test
    fun `in case of the first time, state is init`() = runWithTestDispatcher {
        //given
        val expectedState = FakeData.initialState

        //when
        sut.state.test {
            val state = awaitItem()
            //then
            Truth.assertThat(state).isEqualTo(expectedState)
        }
    }

    @Test
    fun `given the state exposed is the default one, then when sending Initialize action, verify invocation of usecase`() = runWithTestDispatcher {
        //given
        val action = MoviesAction.Initialize
        coEvery { getMoviesUseCaseMock(any()) } returns Outcome.Success(listOf())

        //when
        sut.onAction(action)
        runCurrent() //runs all pending tasks

        //then
        coVerify { getMoviesUseCaseMock(any()) }
    }


    @Test
    fun `given the state exposed is the default one, then when sending GetMovies action, verify invocation of usecase`() = runWithTestDispatcher {
        //given
        val action = MoviesAction.GetMovies
        coEvery { getMoviesUseCaseMock(any()) } returns Outcome.Success(listOf())

        //when
        sut.onAction(action)
        advanceUntilIdle()

        //then
        coVerify { getMoviesUseCaseMock(any()) }
    }

    @Test
    fun `when sending GetMovies action, one of the states exposed contains isLoading = true`() = runWithTestDispatcher {
        //given
        //no setup needed
        val action = MoviesAction.GetMovies
        val expectedState = FakeData.loadingState
        coEvery { getMoviesUseCaseMock(any()) } returns null.toSuccessfulOutcome()

        //when
        sut.state.test {
            sut.onAction(action)
            ///////
            awaitItem().also {
                println("this is the initial state[$it]")
            }
            //or
            //skipItems(1) //skip initial state
            ///////
            val secondState = awaitItem()

            ///////
            awaitItem().also {
                println("this is the final exposed state for this action[$it]")
            }
            //skipItems(1) //skip final state
            Truth.assertThat(secondState).isEqualTo(expectedState)
            ///////
        }
    }

//    @Test
//    fun`when sending GetMovies action,then final exposed state contains movies`() = runWithTestDispatcher {
//        //given

//        val action = MoviesAction.GetMovies
//        val results = FakeData.successOutcome
//        val expectedState = FakeData.moviesState
//        coEvery { getMoviesUseCaseMock(any()) } returns results
//
//
//        //when
//        sut.state.test {
//            sut.onAction(action)
//            skipItems(2)
//            val state = awaitItem()
//            Truth.assertThat(state).isEqualTo(expectedState)
//            advanceUntilIdle()
//        }
//    }

    @Test
    fun`same as above, but another approach for educational purpose --- when sending GetMovies action,then final exposed state contains movies`() = runWithTestDispatcher {
        //given
        //no setup needed
        val action = MoviesAction.GetMovies
        val expectedState = FakeData.moviesState
        coEvery { getMoviesUseCaseMock(any()) } returns FakeData.successOutcome

        //when
        sut.state.test {
            sut.onAction(action)
            skipItems(2)
            val state = awaitItem()

            Truth.assertThat(state).isEqualTo(expectedState)
        }
    }

    @Test
    fun `when getting movies returns null then state is set to no results`() = runWithTestDispatcher {
        //given
        //no setup needed
        val action = MoviesAction.GetMovies
        val expectedState = noMoviesState
        val results = null
        coEvery { getMoviesUseCaseMock(any()) } returns results.toSuccessfulOutcome()

        //when
        sut.state.test {
            sut.onAction(action)
            skipItems(2)
            val emittedState = awaitItem()
            Truth.assertThat(emittedState).isEqualTo(expectedState)
        }
    }

    @Test
    fun `when getting movies returns error then effect is set to show toast`() = runWithTestDispatcher {
        //given
        //no setup needed
        val action = MoviesAction.GetMovies

        coEvery { getMoviesUseCaseMock(any()) } returns  Outcome.Error("mock error")

        //when
        sut.events.test {
            sut.onAction(action)
            val emittedEffect = awaitItem()
            //then
            Truth.assertThat(emittedEffect).isInstanceOf(MoviesEvent.ErrorToast::class.java)
        }
    }


    @Test
    fun `when clicking on a movie, verify that on listToDetailAction was called`() = runWithTestDispatcher {
        //given
        //no setup needed
        val action = MoviesAction.OnMovieSelection(1)
        every { listToDetailAction(any()) } returns Unit

        //when
        sut.onAction(action)
        advanceUntilIdle()

        verify { listToDetailAction(any()) }
    }
}