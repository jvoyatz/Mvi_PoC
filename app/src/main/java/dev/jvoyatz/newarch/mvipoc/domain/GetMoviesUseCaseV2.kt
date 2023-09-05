package dev.jvoyatz.newarch.mvipoc.domain

import dev.jvoyatz.newarch.mvipoc.data.MoviesRepository
import dev.jvoyatz.newarch.mvipoc.outcome.Outcome

fun interface GetMoviesUseCaseV2: (Int) -> kotlinx.coroutines.flow.Flow<Outcome<List<Movie>>>