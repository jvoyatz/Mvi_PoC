package dev.jvoyatz.newarch.mvipoc.data.sources.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class MovieEntity(
    val title: String,
    val poster: String,
    val overview: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}