package dev.jvoyatz.newarch.mvipoc.data.sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MoviesDao {

    @Query("SELECT * FROM MovieEntity")
    abstract fun getMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMovies(movies: List<MovieEntity>)
}