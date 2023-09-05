package dev.jvoyatz.newarch.mvipoc.data.sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Query("SELECT * FROM MovieEntity")
    fun getMovies(): Flow<List<MovieEntity>>

    @Insert
    fun insert(movies: List<MovieEntity>)
}