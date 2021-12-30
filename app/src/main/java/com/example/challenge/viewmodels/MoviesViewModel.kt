package com.example.challenge.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import com.example.challenge.repository.Repository

open class MoviesViewModel @ViewModelInject constructor(
    val repository: Repository) : BaseViewModel() {

    val movieListObservable
        get() = repository.getMovieListObservable()

    val movieDetailsObservable
        get() = repository.getMovieDetailsObservable()


    init {
        compositeDisposable = repository.getCompositeDisposableObject()
    }

    /**
     * Fetch movie items
     **/
    fun getMovieList(searchTerm:String, pageNum:Int) {
        repository.getMovieList(searchTerm,pageNum)
    }

    /**
     * Fetch movie
     **/
    fun getMovieDetails(omdbId:String) {
        repository.getMovieDetails(omdbId = omdbId)
    }
}