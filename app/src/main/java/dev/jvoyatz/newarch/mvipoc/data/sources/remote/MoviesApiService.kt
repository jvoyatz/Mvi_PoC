package dev.jvoyatz.newarch.mvipoc.data.sources.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): Response<MovieResponse>


    @GET("movie/top_rated")
    suspend fun getTopRatedMovies2(
        @Query("api_key") api_key: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MovieResponse

    companion object {

        private var retrofitService: MoviesApiService? = null

        fun getInstance(): MoviesApiService {
            if (retrofitService == null) {

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                  /*  .client(
                        OkHttpClient.Builder()
                            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build()
                    )*/
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(MoviesApiService::class.java)
            }
            return retrofitService!!
        }
    }
}