package dev.jvoyatz.newarch.mvipoc.domain

import dev.jvoyatz.newarch.mvipoc.outcome.Outcome

fun interface GetMoviesUseCaseV3: (Int) -> kotlinx.coroutines.flow.Flow<Outcome<List<Movie>>>