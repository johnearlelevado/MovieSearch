package com.example.challenge.repository

import com.example.challenge.api.omdb.dto.Movie
import com.example.challenge.api.omdb.dto.MovieSearchResult
import retrofit2.Response

interface Repository {

    suspend fun getMovieList(searchTerm:String, pageNum:Int): Response<MovieSearchResult>

    suspend fun getMovieDetails(omdbId:String): Response<Movie>
}