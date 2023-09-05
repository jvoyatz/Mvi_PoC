package dev.jvoyatz.newarch.mvipoc.domain

import dev.jvoyatz.newarch.mvipoc.outcome.Outcome
import gr.jvoyatz.android.poc.mvi.domain.Movie

fun interface GetMoviesUseCase: suspend (Int) -> Outcome<List<Movie>>