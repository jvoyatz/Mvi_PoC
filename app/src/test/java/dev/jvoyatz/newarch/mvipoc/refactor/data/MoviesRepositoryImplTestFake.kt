package dev.jvoyatz.newarch.mvipoc.refactor.data

import com.google.common.truth.Truth
import dev.jvoyatz.newarch.mvipoc.fakes.FakeApi
import dev.jvoyatz.newarch.mvipoc.fakes.FakeData
import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.Outcome
import dev.jvoyatz.newarch.mvipoc.refactor.domain.repository.MoviesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class MoviesRepositoryImplTestFake {

    //setup
    private val api = FakeApi()
    //system under test
    private lateinit var sut: MoviesRepository

    @Before
    fun setup(){
        sut = MoviesRepositoryImpl(api)
    }

    @Test
    fun `given normal conditions, when getting movies, returns success`() = runTest{
        //given
        api.responseBlock = {
            FakeData.success
        }

        //when
        val result = sut.getMovies(1)

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
        api.responseBlock = {
            FakeData.httpError
        }

        //when
        val result = sut.getMovies(1)

        //then
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result).isInstanceOf(Outcome.Error::class.java)
        val message = (result as Outcome.Error).message
        Truth.assertThat(message).isNotNull()
    }

    @Test
    fun `given that servers returns an io error response, when getting movies, returns error`() = runTest{
        //given
        api.responseBlock = {
            throw UnknownHostException("cannot find ip")
        }

        //when
        val result = sut.getMovies(1)

        //then
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result).isInstanceOf(Outcome.Error::class.java)
        val message = (result as Outcome.Error).message
        Truth.assertThat(message).isNotNull()
        Truth.assertThat(message).contains("cannot find ip")
    }

}