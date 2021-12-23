package com.example.challenge.repository

import androidx.lifecycle.MutableLiveData
import com.example.challenge.api.common.ApiResponse
import com.example.challenge.api.omdb.dto.Movie
import com.example.challenge.api.omdb.dto.MovieSearchResult
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable

interface Repository {

    fun getCompositeDisposableObject(): CompositeDisposable

    fun getMovieList(searchTerm:String, pageNum:Int)

    fun getMovieDetails(omdbId:String)

    fun getMovieListObservable(): MutableLiveData<ApiResponse<MovieSearchResult>>

    fun getMovieDetailsObservable(): MutableLiveData<ApiResponse<Movie>>
}