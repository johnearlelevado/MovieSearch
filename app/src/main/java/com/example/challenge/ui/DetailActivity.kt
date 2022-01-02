package com.example.challenge.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.challenge.R
import com.example.challenge.api.common.ApiResponse
import com.example.challenge.api.common.Status
import com.example.challenge.api.omdb.dto.Movie
import com.example.challenge.api.omdb.dto.Search
import com.example.challenge.databinding.ActivityDetailBinding
import com.example.challenge.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity() {

    lateinit var binding : ActivityDetailBinding
    private val moviesViewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie = intent.extras?.getParcelable<Search>("movie")

        Glide.with(this)
            .load(movie?.poster)
            .error(R.drawable.img_placeholder)
            .into(binding.imgPoster)

        binding.tvTitle.text = "${movie?.title}"
        binding.imgClose.setOnClickListener {
            finish()
        }

        moviesViewModel.movieDetailsResponseLiveData.observe(this, Observer {
            handleResponse(it)
        })
        moviesViewModel.getMovieDetails(omdbId = movie?.imdbID!!)
    }

    private fun handleResponse(apiResponse: ApiResponse<Movie>?) {
        when(apiResponse?.mStatus) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                val response = apiResponse?.mResponse
                binding.tvActors.text = "<b>Actor:</b> \t${response?.actors}".toHtml()
                binding.tvLanguages.text = "<b>Language:</b> \t${response?.language}".toHtml()
                binding.tvPlot.text = "<b>Plot:</b> \t${response?.plot}".toHtml()
                binding.tvYear.text = "<b>Year:</b> \t${response?.year}".toHtml()
                binding.tvTitle.text = "<b>Title:</b> \t${response?.title}".toHtml()
            }
            Status.FAIL, Status.ERROR -> {
                showToast("Something went wrong")
            }
        }
    }

    private fun String.toHtml() = HtmlCompat.fromHtml(this,HtmlCompat.FROM_HTML_MODE_LEGACY)
}