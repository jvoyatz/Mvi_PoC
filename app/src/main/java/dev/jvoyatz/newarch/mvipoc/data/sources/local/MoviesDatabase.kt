package dev.jvoyatz.newarch.mvipoc.data.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import timber.log.Timber
import java.util.concurrent.Executors

@Database(entities = [MovieEntity::class], version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao() : MoviesDao

    companion object {
        private const val TAG = "DATABASE"
        private const val MSG = "query: %s | args: %s"
        private const val DATABASE = "MOVIESDB"

        fun getDatabase(context: Context): MoviesDatabase {
            return Room.inMemoryDatabaseBuilder(
                context,
                MoviesDatabase::class.java,
            ).apply {
                setQueryCallback(object : RoomDatabase.QueryCallback {
                    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                        Timber.tag(TAG).d(MSG, sqlQuery, bindArgs)
                    }
                }, Executors.newSingleThreadExecutor())
            }.build()
        }

    }
}