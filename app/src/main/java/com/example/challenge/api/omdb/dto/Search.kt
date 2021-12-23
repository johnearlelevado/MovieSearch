package com.example.challenge.api.omdb.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Search (
	@SerializedName("Title") val title : String,
	@SerializedName("Year") val year : String,
	@SerializedName("imdbID") val imdbID : String,
	@SerializedName("Type") val type : String,
	@SerializedName("Poster") val poster : String
):Parcelable