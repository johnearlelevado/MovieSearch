package com.example.challenge.repository

import androidx.lifecycle.MutableLiveData
import com.example.challenge.BuildConfig
import com.example.challenge.api.common.ApiResponse
import com.example.challenge.api.common.scheduler.SchedulerProvider
import com.example.challenge.api.omdb.dto.Movie
import com.example.challenge.api.omdb.dto.MovieSearchResult
import com.example.challenge.api.omdb.service.OmdbApiService
import io.reactivex.disposables.CompositeDisposable

class RepositoryImpl constructor(
    private val omdbApiService: OmdbApiService
): Repository {

    /**
     * Fetch movie details from the network
     **/
    override suspend fun getMovieDetails(omdbId:String) = omdbApiService.getMovieItem(omdbId = omdbId,apikey = BuildConfig.API_KEY)

    /**
     * Fetch items from the network
     **/
    override suspend fun getMovieList(searchTerm:String, pageNum:Int) = omdbApiService.getMovieList(
        searchTerm = searchTerm,
        page = pageNum,
        apikey = BuildConfig.API_KEY
    )

}