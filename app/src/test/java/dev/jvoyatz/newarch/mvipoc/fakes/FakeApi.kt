package dev.jvoyatz.newarch.mvipoc.fakes

import dev.jvoyatz.newarch.mvipoc.refactor.data.remote.MovieResponse
import dev.jvoyatz.newarch.mvipoc.refactor.data.remote.MoviesApi
import retrofit2.Response

class FakeApi: MoviesApi {
    var responseBlock: (() -> Response<MovieResponse>)? = null
    override suspend fun getTopRatedMovies(
        api_key: String,
        language: String,
        page: Int
    ): Response<MovieResponse> = responseBlock!!.invoke()
}