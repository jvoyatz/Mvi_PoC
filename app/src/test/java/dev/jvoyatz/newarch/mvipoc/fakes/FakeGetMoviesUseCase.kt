package dev.jvoyatz.newarch.mvipoc.fakes

import dev.jvoyatz.newarch.mvipoc.refactor.core.utils.Outcome
import dev.jvoyatz.newarch.mvipoc.refactor.domain.models.Movie
import dev.jvoyatz.newarch.mvipoc.refactor.domain.usecases.GetMoviesUseCase

class FakeGetMoviesUseCase: GetMoviesUseCase {

    var called: Boolean = false
    var outcome: Outcome<List<Movie>> ? = null
        set(value) {
            println("!!!!!!!!!! $value")
            field = value
        }
    override suspend fun invoke(p1: Int): Outcome<List<Movie>> {
        called = true
        return  outcome!!
    }
}