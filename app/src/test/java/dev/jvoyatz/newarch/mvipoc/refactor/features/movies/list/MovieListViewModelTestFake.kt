package dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth
import dev.jvoyatz.newarch.mvipoc.TestDispatcherRule
import dev.jvoyatz.newarch.mvipoc.TestUtils
import dev.jvoyatz.newarch.mvipoc.TestUtils.runWithTestDispatcher
import dev.jvoyatz.newarch.mvipoc.fakes.FakeData
import dev.jvoyatz.newarch.mvipoc.fakes.FakeGetMoviesUseCase
import dev.jvoyatz.newarch.mvipoc.fakes.FakeMovieListToDetailAction
import dev.jvoyatz.newarch.mvipoc.refactor.core.navigation.NavigationCoordinator
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.Outcome
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.OutcomeExtensions.toSuccessfulOutcome
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesAction
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesEvent
import io.mockk.coEvery
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieListViewModelTestFake{

    @get:Rule
    val testDispatcherRule: TestDispatcherRule = TestDispatcherRule()

    //private val testScope = TestScope(testDispatcherRule.testDispatcher)
    private lateinit var listToDetailAction: FakeMovieListToDetailAction
    private lateinit var navigationCoordinator: NavigationCoordinator
    private lateinit var getMoviesUseCase: FakeGetMoviesUseCase
    private lateinit var sut: MovieListViewModel

    @Before
    fun setup(){
        println("before ")
        getMoviesUseCase = FakeGetMoviesUseCase()
        getMoviesUseCase.outcome = null
        getMoviesUseCase.called = false
        listToDetailAction = FakeMovieListToDetailAction()
        listToDetailAction.wasCalled = false
        //navigationCoordinator = NavigationCoordinator(testScope)


        sut = MovieListViewModel(FakeData.fakeSavedStateHandle, getMoviesUseCase, listToDetailAction /*, navigationCoordinator*/)
    }
    @After
    fun clear(){
        getMoviesUseCase.outcome = null
        getMoviesUseCase.called = false
        listToDetailAction.wasCalled = false
    }

    @Test
    fun `when initialized, state's fields are set to their default value`() = runTest {
        //given
        val expectedState = FakeData.initialState
        val sut = MovieListViewModel(SavedStateHandle(mapOf("test" to 1)), FakeGetMoviesUseCase(), listToDetailAction /*, navigationCoordinator*/)

        //when
        sut.state.test {
            //then
            Truth.assertThat(awaitItem()).isEqualTo(expectedState)
        }
    }

    @Test
    fun `after vm creation & exposure of the init state, onAction() is called with Init , then verify getMoviesUseCase() is called`() = runTest {
        //given
        val action = MoviesAction.Initialize
        getMoviesUseCase.outcome = FakeData.successOutcome
        //when
        sut.onAction(action)

        //then
        Truth.assertThat(getMoviesUseCase.called).isTrue()
        getMoviesUseCase.outcome = null
    }

    @Test
    fun `after vm creation & exposure of the init state, onAction() is called with GetMovies , then verify getMoviesUseCase() is called`() = runTest {
        //given
        val action = MoviesAction.GetMovies
        getMoviesUseCase.outcome = FakeData.successOutcome

        //when
        sut.onAction(action)

        //then
        Truth.assertThat(getMoviesUseCase.called).isTrue()
    }


    @Test
    fun `after vm creation & exposure of the init state, onAction() is called with Init, then verify that result is the same as the mocked data`() = runTest {
        //given
        val action = MoviesAction.Initialize
        getMoviesUseCase.outcome = FakeData.successOutcome
        val expectedState = FakeData.moviesState

        //when
        sut.onAction(action)

        //then
        sut.state.test {
            val emission = awaitItem()
            Truth.assertThat(emission).isEqualTo(expectedState)
        }
    }

    @Test
    fun`when sending GetMovies action,then final exposed state contains movies`() = runTest {
        //given
        //no setup needed
        val action = MoviesAction.GetMovies
        val results = FakeData.successOutcome
        val expectedState = FakeData.moviesState
        getMoviesUseCase.outcome = results


        //when
        sut.state.test {
            sut.onAction(action)
            skipItems(1)
            val state = awaitItem()
            Truth.assertThat(state).isEqualTo(expectedState)
        }
    }

    @Test
    fun `when getting movies returns null then state is set to no results`() = runTest {
        //given
        val action = MoviesAction.GetMovies
        val expectedState = FakeData.noMoviesState
        getMoviesUseCase.outcome = FakeData.emptyOutcome

        //when
        sut.state.test {
            sut.onAction(action)
            skipItems(1)
            val emittedState = awaitItem()
            Truth.assertThat(emittedState).isEqualTo(expectedState)
        }
    }

    @Test
    fun `when getting movies returns error then effect is set to show toast`() = runTest {
        //given
        //no setup needed
        val action = MoviesAction.GetMovies
        getMoviesUseCase.outcome = FakeData.errorOutcome

        //when
        sut.events.test {
            sut.onAction(action)
            val emittedEffect = awaitItem()

            //then
            Truth.assertThat(emittedEffect).isInstanceOf(MoviesEvent.ErrorToast::class.java)
        }
    }


    @Test
    fun `when clicking on a movie, verify that on listToDetailAction was called`() = runTest {
        //given
        //no setup needed
        val action = MoviesAction.OnMovieSelection(1)

        //when
        sut.onAction(action)


        //then
        Truth.assertThat(listToDetailAction.wasCalled).isTrue()
    }
}