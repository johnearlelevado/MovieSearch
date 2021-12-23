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
    private val omdbApiService: OmdbApiService,
    private val schedulerProvider: SchedulerProvider
): Repository {
    val movieListResponseLiveData = MutableLiveData<ApiResponse<MovieSearchResult>>()
    val movieDetailsResponseLiveData = MutableLiveData<ApiResponse<Movie>>()
    val compositeDisposable = CompositeDisposable()


    /**
     * Fetch movie details from the network
     **/
    override fun getMovieDetails(omdbId:String) {
        compositeDisposable.add(omdbApiService.getMovieItem(omdbId = omdbId,apikey = BuildConfig.API_KEY)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe {movieDetailsResponseLiveData.value = ApiResponse.loading() }
            .subscribe({ response ->
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
            }, { t ->
                t.printStackTrace()
                movieDetailsResponseLiveData.value = ApiResponse.fail(t)
            }))
    }

    /**
     * Fetch items from the network
     **/
    override fun getMovieList(searchTerm:String, pageNum:Int) {
        val service = omdbApiService.getMovieList(
            searchTerm = searchTerm,
            page = pageNum,
            apikey = BuildConfig.API_KEY
        )

        compositeDisposable.add(service
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe {movieListResponseLiveData.value = ApiResponse.loading() }
            .subscribe({ response ->
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
            }, { t ->
                t.printStackTrace()
                movieListResponseLiveData.value = ApiResponse.fail(t)
            }))
    }

    override fun getCompositeDisposableObject(): CompositeDisposable {
        return compositeDisposable
    }

    override fun getMovieListObservable(): MutableLiveData<ApiResponse<MovieSearchResult>> {
        return movieListResponseLiveData
    }

    override fun getMovieDetailsObservable(): MutableLiveData<ApiResponse<Movie>> {
        return movieDetailsResponseLiveData
    }
}