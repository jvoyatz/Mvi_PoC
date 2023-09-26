package dev.jvoyatz.newarch.mvipoc.refactor.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Movie(val title: String, val poster: String, val overview: String): Parcelable{
    var id = -1
}