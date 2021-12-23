package com.example.challenge.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.challenge.api.common.ApiResponse
import com.example.challenge.api.common.Status
import com.example.challenge.api.common.scheduler.SchedulerProvider
import com.example.challenge.api.omdb.dto.Movie
import com.example.challenge.api.omdb.dto.MovieSearchResult
import com.example.challenge.api.omdb.dto.Search
import com.example.challenge.api.omdb.service.OmdbApiService
import com.example.challenge.repository.RepositoryImpl
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class MoviesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    companion object {
        @ClassRule
        @JvmField
        val schedulers =
            RxImmediateSchedulerRule()
    }

    @Mock
    lateinit var observerMovieList: Observer<ApiResponse<MovieSearchResult>>

    @Mock
    lateinit var observerMovieDetails: Observer<ApiResponse<Movie>>

    private lateinit var viewModel: MoviesViewModel

    lateinit var list : ArrayList<Search>
    lateinit var schedulerProviderMock : SchedulerProvider

    @Before
    fun setup() {
        list = ArrayList()
        list.add(
            Search(
                title = "fake-title",
                type = "fake-type",
                poster = "fake-poster",
                year = 2021,
                imdbID = "fake-id"
            )
        )

        schedulerProviderMock = mock<SchedulerProvider>(){
            on { io() } doReturn Schedulers.io()
            on { ui() } doReturn AndroidSchedulers.mainThread()
        }
    }

    @Test
    fun `fetch movie details successfully`() {
        setupSuccessResponse()

        viewModel.getMovieDetails(omdbId = "fake-id")
        viewModel.movieDetailsObservable.observeForever(observerMovieDetails)
        val value = viewModel.movieDetailsObservable.value
        assertThat(value?.mStatus).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `fetch movie list successfully`() {
        setupSuccessResponse()
        viewModel.getMovieList(searchTerm = "fake-term",pageNum = 0)
        viewModel.movieListObservable.observeForever(observerMovieList)
        val value = viewModel.movieListObservable.value
        assertThat(value?.mStatus).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `fetch movie details failed`() {
        setupFailedResponse()

        viewModel.getMovieDetails(omdbId = "fake-id")
        viewModel.movieDetailsObservable.observeForever(observerMovieDetails)
        val value = viewModel.movieDetailsObservable.value
        assertThat(value?.mStatus).isEqualTo(Status.ERROR)
    }

    @Test
    fun `fetch movie list failed`() {
        setupFailedResponse()

        viewModel.getMovieList(searchTerm = "fake-term",pageNum = 0)
        viewModel.movieListObservable.observeForever(observerMovieList)
        val value = viewModel.movieListObservable.value
        assertThat(value?.mStatus).isEqualTo(Status.ERROR)
    }

    private fun setupSuccessResponse() {
        val response: Response<MovieSearchResult> = Response.success(MovieSearchResult(search = listOf(),totalResults = 0,response = false))
        val movieResponse: Response<Movie> = Response.success(Movie(
            imdbID = "fake-id",
            year = 2021,
            title = "fake-title",
            rated = "fake-rating",
            response = false,
            released = "fake-released",
            runtime = "fake-runtime",
            genre = "fake-genre",
            director = "fake-director",
            website = "fake-website",
            writer = "fake-writer",
            actors = "fake-actors",
            plot = "fake-plot",
            language = "fake-language",
            country = "fake-country",
            awards = "fake-awards",
            poster = "fake-poster",
            metascore = "fake-metascore",
            imdbRating = 0.0,
            type = "fake-type",
            dVD = "fake-dvd",
            boxOffice = "fake-boxoffice",
            production = "fake-production"
        ))
        val crimeDataServiceMock = mock<OmdbApiService> {
            on { getMovieItem(omdbId = any(), apikey = any()) } doReturn Observable.just(movieResponse)

            on { getMovieList(searchTerm = any() ,page = any(),apikey = any()) } doReturn Observable.just(
                response
            )

        }
        val repository = RepositoryImpl(crimeDataServiceMock, schedulerProviderMock)
        viewModel = MoviesViewModel(repository)
    }

    private fun setupFailedResponse() {
        val movieResponse: Response<Movie> = Response.error(404,
            ResponseBody.create(
                MediaType.parse("application/json"),
                "{}"))
        val movieListResponse: Response<MovieSearchResult> = Response.error(404,
            ResponseBody.create(
                MediaType.parse("application/json"),
                "{}"))
        val crimeDataServiceMock = mock<OmdbApiService> {
            on { getMovieItem(omdbId = any(), apikey = any()) } doReturn Observable.just(movieResponse)
            on { getMovieList(searchTerm = any() ,page = any(),apikey = any()) } doReturn Observable.just(movieListResponse)
        }
        val repository = RepositoryImpl(crimeDataServiceMock, schedulerProviderMock)
        viewModel = MoviesViewModel(repository)
    }
}
