package com.example.challenge.api.omdb.dto

import com.google.gson.annotations.SerializedName

data class MovieSearchResult (
	@SerializedName("Search") val search : List<Search>,
	@SerializedName("totalResults") val totalResults : Int,
	@SerializedName("Response") val response : Boolean
)