package dev.jvoyatz.newarch.mvipoc.refactor.data.remote

import com.google.common.truth.Truth
import dev.jvoyatz.newarch.mvipoc.TestDispatcherRule
import dev.jvoyatz.newarch.mvipoc.fakes.FakeData
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.*
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoviesApiTest {

    private lateinit var webServer: MockWebServer
    private lateinit var api: MoviesApi

    @get:Rule
    val rule = TestDispatcherRule()

    @Before
    fun setup() {
        webServer = MockWebServer()
        webServer.start()
        val url = webServer.url("/")
        val retrofit = Retrofit.Builder()
            .baseUrl(url).client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(MoviesApi::class.java)
    }
    @After
    fun shutdown() {
        webServer.shutdown()
    }

    @Test
    fun `fetching data -- happy (200) path `() = runTest{
        //given
        webServer.enqueue(MockResponse().setResponseCode(200).setBody(FakeData.mockedApiResponse))

        //when
        val response = api.getTopRatedMovies("key", page = 1)

        //then
        Truth.assertThat(response.isSuccessful).isTrue()
        Truth.assertThat(response.code()).isEqualTo(200)
        val body = response.body()
        Truth.assertThat(body).isNotNull()
        Truth.assertThat(body!!.results).hasSize(20)
    }

    @Test
    fun `fetching data -- http 400 error `() = runTest{
        //given
        webServer.enqueue(MockResponse().setResponseCode(400).setBody(FakeData.error))

        //when
        val response = api.getTopRatedMovies("key", page = 1)

        //then
        Truth.assertThat(response.isSuccessful).isFalse()
        Truth.assertThat(response.code()).isEqualTo(400)
    }

    @Test
    fun `fetching data -- ioException 500`() = runTest{
        //given
        webServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST))

        //when
        assertThrows(IOException::class.java) {
          runBlocking {
              //then
              api.getTopRatedMovies("key", page = 1)
          }
        }
    }

    @Test
    fun `fetching data -- other exception - error but success response`() = runTest{
        //given
        webServer.enqueue(MockResponse().setBody("this an error").setResponseCode(200))

        //when
        assertThrows(com.google.gson.stream.MalformedJsonException::class.java) {
            runBlocking {
                //then
                api.getTopRatedMovies("key", page = 1)
            }
        }
    }
}