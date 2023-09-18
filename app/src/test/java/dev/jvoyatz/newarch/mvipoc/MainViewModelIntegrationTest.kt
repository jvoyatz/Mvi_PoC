package dev.jvoyatz.newarch.mvipoc

import app.cash.turbine.test
import com.google.common.truth.Truth
import dev.jvoyatz.newarch.mvipoc.data.API_KEY
import dev.jvoyatz.newarch.mvipoc.data.MoviesRepository
import dev.jvoyatz.newarch.mvipoc.data.sources.remote.MovieDto
import dev.jvoyatz.newarch.mvipoc.data.sources.remote.MovieResponse
import dev.jvoyatz.newarch.mvipoc.data.sources.remote.MoviesApiService
import dev.jvoyatz.newarch.mvipoc.data.sources.MoviesMappers.dtoToMovies
import dev.jvoyatz.newarch.mvipoc.data.sources.local.MovieEntity
import dev.jvoyatz.newarch.mvipoc.data.sources.local.MoviesDao
import dev.jvoyatz.newarch.mvipoc.domain.GetMoviesUseCase
import dev.jvoyatz.newarch.mvipoc.presentation.screen1.MainActivityContract
import dev.jvoyatz.newarch.mvipoc.presentation.screen1.MainViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelIntegrationTest {

    @get:Rule
    val rule = TestDispatcherRule()


    private val apiService: MoviesApiService = mockk()
    private val fakeMoviesDao: MoviesDao = object : MoviesDao() {
        override fun getMovies(): Flow<List<MovieEntity>> {
            TODO("Not yet implemented")
        }

        override suspend fun insertMovies(movies: List<MovieEntity>) {
            TODO("Not yet implemented")
        }

    }
    private val moviesRepository: MoviesRepository = MoviesRepository(apiService, fakeMoviesDao)
    private val getMoviesUseCase: GetMoviesUseCase = GetMoviesUseCase {
        moviesRepository.getMovies(it)
    }
    private val initMainViewState = MainActivityContract.MainViewState.Init

    //subject under test
    private lateinit var sut: MainViewModel

    @Before
    fun setup() {
        sut = MainViewModel(
            getMoviesUseCase,
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

    @Test
    fun `when handling init event then get movies`() = runTest {
        //given
        //no setup needed
        val event = MainActivityContract.Event.Init
        val moviesResponse = MovieResponse(1, listOf())
        coEvery { apiService.getTopRatedMovies(any(), any(), any()) } returns Response.success(
            moviesResponse
        )

        //when
        sut.postEvent(event)

        //then
        coVerify { apiService.getTopRatedMovies(API_KEY, "en-US", 1) }
    }

    @Test
    fun `when handling get movies event then get movies`() = runTest {
        //given
        //no setup needed
        val event = MainActivityContract.Event.GetMovies(1)
        val moviesResponse = MovieResponse(1, listOf())
        coEvery { apiService.getTopRatedMovies(any(), any(), any()) } returns Response.success(
            moviesResponse
        )

        //when
        sut.postEvent(event)

        //then
        coVerify { apiService.getTopRatedMovies(API_KEY, "en-US", 1) }
    }

    @Test
    fun `when getting movies then state is set to loading`() = runTest {
        //given
        //no setup needed
        val event = MainActivityContract.Event.GetMovies(1)
        val moviesResponse = MovieResponse(1, listOf())
        coEvery { apiService.getTopRatedMovies(any(), any(), any()) } returns Response.success(
            moviesResponse
        )

        //when
        sut.state().test {
            sut.postEvent(event)
            skipItems(1)
            val emittedState = awaitItem().mainViewState
            skipItems(1)

            //then
            Truth.assertThat(emittedState).isEqualTo(MainActivityContract.MainViewState.Loading)
        }
    }

    @Test
    fun `when getting movies then state is set to results`() = runTest {
        //given
        //no setup needed
        val event = MainActivityContract.Event.GetMovies(1)
        val results = listOf<MovieDto>()
        val resultsDomain = results.dtoToMovies()

        val moviesResponse = MovieResponse(1, results)
        coEvery { apiService.getTopRatedMovies(any(), any(), any()) } returns Response.success(
            moviesResponse
        )

        //when
        sut.state().test {
            sut.postEvent(event)
            skipItems(2)
            val emittedState = awaitItem().mainViewState
            Truth.assertThat(emittedState)
                .isEqualTo(MainActivityContract.MainViewState.Results(resultsDomain))
        }
    }


    @Test
    fun `when getting movies returns error then effect is set to show toast`() = runTest {
        //given
        //no setup needed
        val event = MainActivityContract.Event.GetMovies(1)

        coEvery { apiService.getTopRatedMovies(any(), any(), any()) } returns Response.error(
            400,
            ResponseBody.create("application/json".toMediaTypeOrNull(), "")
        )

        //when
        sut.effect().test {
            sut.postEvent(event)
            val emittedEffect = awaitItem()
            //then
            Truth.assertThat(emittedEffect).isEqualTo(MainActivityContract.Effect.ShowError)
        }
    }
}