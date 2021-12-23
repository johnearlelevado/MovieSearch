package com.example.challenge.api.omdb.dto

import com.google.gson.annotations.SerializedName

data class Ratings (
	@SerializedName("Source") val source : String,
	@SerializedName("Value") val value : String
)