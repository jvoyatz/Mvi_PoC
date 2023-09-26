package dev.jvoyatz.newarch.mvipoc.refactor.data.remote

const val NETWORK_PAGE_SIZE = 25

data class MovieResponse(val page: Int, val results: List<MovieDto>)

data class MovieDto(val original_title: String, val poster_path: String, val overview: String){
    var id = -1
}