package dev.jvoyatz.newarch.mvipoc.fakes

import dev.jvoyatz.newarch.mvipoc.refactor.features.movies.list.contract.MovieListToDetailAction

class FakeMovieListToDetailAction: MovieListToDetailAction {

    var wasCalled: Boolean = false
    override fun invoke(p1: Int) {
        wasCalled = true
    }
}