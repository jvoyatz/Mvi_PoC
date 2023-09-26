package dev.jvoyatz.newarch.mvipoc.refactor.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class TEst{}

interface MoviesApi {
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): Response<MovieResponse>


    companion object Factory{

        private var retrofitService: MoviesApi? = null

        fun getInstance(): MoviesApi {
            if (retrofitService == null) {

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .client(
                        OkHttpClient.Builder()
                            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(MoviesApi::class.java)
            }
            return retrofitService!!
        }
    }
}