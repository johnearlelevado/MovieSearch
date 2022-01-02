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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
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

    @ExperimentalCoroutinesApi
    val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var observerMovieList: Observer<ApiResponse<MovieSearchResult>>

    @Mock
    lateinit var observerMovieDetails: Observer<ApiResponse<Movie>>

    lateinit var viewModel: MoviesViewModel

    lateinit var list : ArrayList<Search>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        list = ArrayList()
        list.add(
            Search(
                title = "fake-title",
                type = "fake-type",
                poster = "fake-poster",
                year = "fake-year",
                imdbID = "fake-id"
            )
        )
    }

    @Test
    fun `fetch movie details successfully`() {
        setupSuccessResponse()

        viewModel.getMovieDetails(omdbId = "fake-id")
        viewModel.movieDetailsResponseLiveData.observeForever(observerMovieDetails)
        val value = viewModel.movieDetailsResponseLiveData.value
        assertThat(value?.mStatus).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `fetch movie list successfully`() {
        setupSuccessResponse()
        viewModel.getMovieList(searchTerm = "fake-term",pageNum = 0)
        viewModel.movieListResponseLiveData.observeForever(observerMovieList)
        val value = viewModel.movieListResponseLiveData.value
        assertThat(value?.mStatus).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `fetch movie details failed`() {
        setupFailedResponse()

        viewModel.getMovieDetails(omdbId = "fake-id")
        viewModel.movieDetailsResponseLiveData.observeForever(observerMovieDetails)
        val value = viewModel.movieDetailsResponseLiveData.value
        assertThat(value?.mStatus).isEqualTo(Status.ERROR)
    }

    @Test
    fun `fetch movie list failed`() {
        setupFailedResponse()

        viewModel.getMovieList(searchTerm = "fake-term",pageNum = 0)
        viewModel.movieListResponseLiveData.observeForever(observerMovieList)
        val value = viewModel.movieListResponseLiveData.value
        assertThat(value?.mStatus).isEqualTo(Status.ERROR)
    }

    private fun setupSuccessResponse() {
        val response: Response<MovieSearchResult> = Response.success(MovieSearchResult(search = listOf(),totalResults = 0,response = false))
        val movieResponse: Response<Movie> = Response.success(Movie(
            imdbID = "fake-id",
            year = "fake-year",
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
            imdbRating = "0.0",
            type = "fake-type",
            dVD = "fake-dvd",
            boxOffice = "fake-boxoffice",
            production = "fake-production"
        ))
        val serviceMock = mock<OmdbApiService> {
            on { runBlocking { getMovieItem(omdbId = any(), apikey = any()) } } doReturn movieResponse

            on { runBlocking { getMovieList(searchTerm = any() ,page = any(),apikey = any()) } } doReturn response

        }
        val repository = RepositoryImpl(serviceMock)
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
        val serviceMock = mock<OmdbApiService> {
            on { runBlocking {getMovieItem(omdbId = any(), apikey = any())} } doReturn movieResponse
            on { runBlocking {getMovieList(searchTerm = any() ,page = any(),apikey = any())} } doReturn movieListResponse
        }
        val repository = RepositoryImpl(serviceMock)
        viewModel = MoviesViewModel(repository)
    }
}
