package dev.jvoyatz.newarch.mvipoc.refactor.domain.usecases

import com.google.common.truth.Truth
import dev.jvoyatz.newarch.mvipoc.fakes.FakeData
import dev.jvoyatz.newarch.mvipoc.fakes.FakeMoviesRepository
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.Outcome
import dev.jvoyatz.newarch.mvipoc.refactor.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class GetMovieUseCaseTestMock {

    private lateinit var repository: MoviesRepository

    private lateinit var sut: GetMoviesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        sut = GetMoviesUseCase.create(repository)
    }


    @Test
    fun `given normal conditions, when getting movies, returns success`() = runTest{
        //given
        coEvery { repository.getMovies(any()) } returns FakeData.successOutcome

        //when
        val result = sut(1)

        //then
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result).isInstanceOf(Outcome.Success::class.java)
        val data = (result as Outcome.Success).value
        Truth.assertThat(data).isNotNull()
        Truth.assertThat(data!!.size).isEqualTo(FakeData.movieDtos.size)
    }

    @Test
    fun `given that servers returns an http error response, when getting movies, returns error`() = runTest{
        //given
        coEvery { repository.getMovies(any()) } returns FakeData.errorOutcome

        //when
        val result = sut(1)

        //then
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result).isInstanceOf(Outcome.Error::class.java)
        val message = (result as Outcome.Error).message
        Truth.assertThat(message).isNotNull()
    }

    @Test
    fun `given that servers returns an io error response, when getting movies, returns error`() = runTest{
        //given
        coEvery { repository.getMovies(any()) } coAnswers {FakeData.ioErrorOutcome}

        //when
        val result = sut(1)

        //then
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result).isInstanceOf(Outcome.Error::class.java)
        val message = (result as Outcome.Error).message
        Truth.assertThat(message).isNotNull()
        Truth.assertThat(message).contains("mocked io error")
    }
}