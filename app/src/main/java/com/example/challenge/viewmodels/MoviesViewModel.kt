package com.example.challenge.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challenge.api.common.ApiResponse
import com.example.challenge.api.omdb.dto.Movie
import com.example.challenge.api.omdb.dto.MovieSearchResult
import com.example.challenge.repository.Repository
import kotlinx.coroutines.launch

open class MoviesViewModel @ViewModelInject constructor(
    private val repository: Repository) : BaseViewModel() {

    val movieListResponseLiveData = MutableLiveData<ApiResponse<MovieSearchResult>>()
    val movieDetailsResponseLiveData = MutableLiveData<ApiResponse<Movie>>()


    /**
     * Fetch movie items
     **/
    fun getMovieList(searchTerm:String, pageNum:Int) {
        viewModelScope.launch {
            movieListResponseLiveData.value = ApiResponse.loading()
            val response =  repository.getMovieList(searchTerm,pageNum)
            if (response.isSuccessful) {
                var results = response.body()
                movieListResponseLiveData.value = ApiResponse.success(results)
            } else  {
                try {
                    movieListResponseLiveData.value = ApiResponse.error(response.code())
                } catch (e: Exception) {
                    e.printStackTrace()
                    movieListResponseLiveData.value = ApiResponse.fail(e)
                }
            }
        }

    }

    /**
     * Fetch movie
     **/
    fun getMovieDetails(omdbId:String) {
        viewModelScope.launch {
            movieDetailsResponseLiveData.value = ApiResponse.loading()
            val response = repository.getMovieDetails(omdbId = omdbId)
            if (response.isSuccessful) {
                val results = response.body()
                movieDetailsResponseLiveData.value = ApiResponse.success(results)
            } else  {
                try {
                    movieDetailsResponseLiveData.value = ApiResponse.error(response.code())
                } catch (e: Exception) {
                    e.printStackTrace()
                    movieDetailsResponseLiveData.value = ApiResponse.fail(e)
                }
            }
        }
    }
}