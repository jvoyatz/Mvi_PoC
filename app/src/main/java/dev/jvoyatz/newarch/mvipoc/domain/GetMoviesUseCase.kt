package dev.jvoyatz.newarch.mvipoc.domain

import dev.jvoyatz.newarch.mvipoc.outcome.Outcome

fun interface GetMoviesUseCase: suspend (Int) -> Outcome<List<Movie>>