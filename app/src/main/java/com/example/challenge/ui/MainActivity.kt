package com.example.challenge.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.challenge.api.common.ApiResponse
import com.example.challenge.api.common.Status
import com.example.challenge.api.omdb.dto.MovieSearchResult
import com.example.challenge.api.omdb.dto.Search
import com.example.challenge.databinding.ActivityMainBinding
import com.example.challenge.ui.adapters.SearchResultRVAdapter
import com.example.challenge.util.NetworkStatusUtil
import com.example.challenge.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    private val moviesViewModel: MoviesViewModel by viewModels()
    private lateinit var searchResultRVAdapter: SearchResultRVAdapter
    private lateinit var networkStatusUtil: NetworkStatusUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeRecyclerView()
        initializeSearchView()
        initNetworkConnectionStatusHandler()
        initializeViewModel()
    }

    /**
     * initialize handling for observable/flowable data from Network and DB
     * */
    private fun initializeViewModel() {
        // handle network response
        moviesViewModel.movieListObservable.observe(this, Observer {
            handleResponse(it)
        })
    }

    /**
     *  initialize the SearchView with data from SharedPreferences and set listeners for text changes and submission
     * */
    private fun initializeSearchView() {
        binding.input.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                fetchInitialData()
                hideKeyboard(this@MainActivity, window.decorView)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    previousList = arrayListOf()
                    searchResultRVAdapter.clearResults()
                    binding.flNoResults.isVisible = false
                    binding.list.isVisible = true
                }
                return false
            }
        })
    }

    /**
     * Set RecyclerView LayoutManager and Adapter
     * Set callback when item is clicked
     * Set List Refresh handler
     * */
    private fun initializeRecyclerView() {
        // Search result recycler view
        val layoutManager: StaggeredGridLayoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                StaggeredGridLayoutManager(SPAN_COUNT_PORT, StaggeredGridLayoutManager.VERTICAL)
            } else {
                StaggeredGridLayoutManager(SPAN_COUNT_LAND, StaggeredGridLayoutManager.VERTICAL)
            }
        searchResultRVAdapter = SearchResultRVAdapter(ResultClickListener { result ->
            val i = Intent(this,DetailActivity::class.java)
            i.putExtra("movie",result)
            startActivity(i)
        })
        binding.list.layoutManager = layoutManager
        binding.list.adapter = searchResultRVAdapter
        binding.swipeRefresh.setOnRefreshListener {
            if (!binding.input.query.isNullOrEmpty()) {
                fetchInitialData()
            } else {
                binding.swipeRefresh.isRefreshing = false
            }
        }


        binding.list.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                moviesViewModel.getMovieList("${binding.input.query}",page)
            }
        })
    }

    /**
     * Clear list and fetching first page
     * */
    private fun fetchInitialData() {
        previousList = arrayListOf()
        searchResultRVAdapter.clearResults()
        moviesViewModel.getMovieList("${binding.input.query}", 1)
    }

    /**
     * Handle no network scenario.
     * Show an 'offline' message when the network is not available and
     * retrieve retry the API request when the network is available
     * */
    private fun initNetworkConnectionStatusHandler() {
        networkStatusUtil = NetworkStatusUtil(this) {
            fetchInitialData()
        }.apply {
            build(binding.tvNetworkStatusBar)
        }
    }

    var previousList = arrayListOf<Search>()

    /**
     * handle response from livedata
     * */
    private fun handleResponse(apiResponse: ApiResponse<MovieSearchResult>?) {
        apiResponse?.let {
            when(it.mStatus) {
                Status.SUCCESS -> {
                    binding.swipeRefresh.isRefreshing = false
                    var downloadedList = it.mResponse?.search ?: listOf()
                    processResults(downloadedList)
                    if (previousList.isEmpty()) {
                        if (binding.input.query.isNotEmpty()) {
                            binding.flNoResults.isVisible = true
                            binding.list.isVisible = false
                        }
                    } else {
                        binding.flNoResults.isVisible = false
                        binding.list.isVisible = true
                    }
                    searchResultRVAdapter.replace(previousList)
                }
                Status.ERROR, Status.FAIL -> {
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun processResults(downloadedList: List<Search>) {
        for (x in downloadedList) {
            if (!previousList.contains(x)) previousList.add(x)
        }
    }


    companion object {
        const val SPAN_COUNT_PORT = 3
        const val SPAN_COUNT_LAND = 4
    }
}