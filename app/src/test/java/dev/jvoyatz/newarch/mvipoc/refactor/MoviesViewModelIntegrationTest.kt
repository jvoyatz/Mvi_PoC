package dev.jvoyatz.newarch.mvipoc.refactor

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth
import dev.jvoyatz.newarch.mvipoc.TestDispatcherRule
import dev.jvoyatz.newarch.mvipoc.fakes.FakeData
import dev.jvoyatz.newarch.mvipoc.refactor.data.API_KEY
import dev.jvoyatz.newarch.mvipoc.refactor.data.MoviesRepositoryImpl
import dev.jvoyatz.newarch.mvipoc.refactor.data.remote.MovieResponse
import dev.jvoyatz.newarch.mvipoc.refactor.data.remote.MoviesApi
import dev.jvoyatz.newarch.mvipoc.refactor.domain.repository.MoviesRepository
import dev.jvoyatz.newarch.mvipoc.refactor.domain.usecases.GetMoviesUseCase
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.MovieListViewModel
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MovieListToDetailAction
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesAction
import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MoviesEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelIntegrationTest {

    @get:Rule
    val rule = TestDispatcherRule()

    private val listToDetailAction: MovieListToDetailAction = mockk()
    private val apiService: MoviesApi = mockk()
    private val moviesRepository: MoviesRepository = MoviesRepositoryImpl(apiService)
    private val getMoviesUseCase: GetMoviesUseCase = GetMoviesUseCase.create(moviesRepository)

    //subject under test
    private lateinit var sut: MovieListViewModel

    @Before
    fun setup() {
        sut = MovieListViewModel(
            SavedStateHandle(mapOf("test" to 1)),
            getMoviesUseCase,
            listToDetailAction
        )
    }

    @Test
    fun `in case of the first time, state is init`() = runTest {
        //given

        //when
        sut.state.test {
            val emittedState = awaitItem().also {
                println(it)
            }

            //then
            Truth.assertThat(emittedState).isEqualTo(FakeData.initialState)
        }
    }

    @Test
    fun `when handling init event then get movies`() = runTest {
        //given

        val action = MoviesAction.Initialize
        val moviesResponse = MovieResponse(1, listOf())
        coEvery { apiService.getTopRatedMovies(any(), any(), any()) } returns Response.success(
            moviesResponse
        )

        //when
        sut.onAction(action)

        //then
        coVerify { apiService.getTopRatedMovies(API_KEY, "en-US", 1) }
    }

    @Test
    fun `when handling get movies event then get movies`() = runTest {
        //given

        val action = MoviesAction.GetMovies
        val moviesResponse = MovieResponse(1, listOf())
        coEvery { apiService.getTopRatedMovies(any(), any(), any()) } returns Response.success(
            moviesResponse
        )

        //when
        sut.onAction(action)

        //then
        coVerify { apiService.getTopRatedMovies(API_KEY, "en-US", any()) }
    }

    @Test
    fun `when getting movies then state is set to loading`() = runTest {
        //given

        val action = MoviesAction.GetMovies
        val moviesResponse = FakeData.movieResponse
        coEvery { apiService.getTopRatedMovies(any(), any(), any()) } returns Response.success(
            moviesResponse
        )

        //when
        sut.state.test {
            sut.onAction(action)
            skipItems(1)
            val emittedState = awaitItem().also {
                println(it)
            }

            //then
            Truth.assertThat(emittedState).isEqualTo(FakeData.moviesState)
        }
    }


    @Test
    fun `when getting movies returns error then effect is set to show toast`() = runTest {
        //given
        val action = MoviesAction.GetMovies
        coEvery { apiService.getTopRatedMovies(any(), any(), any()) } returns FakeData.httpError

        //when
        sut.events.test {
            sut.onAction(action)
            val emittedEffect = awaitItem()
            //then
            Truth.assertThat(emittedEffect).isInstanceOf(MoviesEvent.ErrorToast::class.java)
        }
    }

}