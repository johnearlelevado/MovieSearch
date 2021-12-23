package com.example.challenge.api.omdb.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movie")
data class Movie (
	@PrimaryKey
	@SerializedName("imdbID") val imdbID : String,
	@SerializedName("Title") val title : String,
	@SerializedName("Year") val year : String,
	@SerializedName("Rated") val rated : String,
	@SerializedName("Released") val released : String,
	@SerializedName("Runtime") val runtime : String,
	@SerializedName("Genre") val genre : String,
	@SerializedName("Director") val director : String,
	@SerializedName("Writer") val writer : String,
	@SerializedName("Actors") val actors : String,
	@SerializedName("Plot") val plot : String,
	@SerializedName("Language") val language : String,
	@SerializedName("Country") val country : String,
	@SerializedName("Awards") val awards : String,
	@SerializedName("Poster") val poster : String,
	@SerializedName("Metascore") val metascore : String,
	@SerializedName("imdbRating") val imdbRating : String,
	@SerializedName("Type") val type : String,
	@SerializedName("DVD") val dVD : String,
	@SerializedName("BoxOffice") val boxOffice : String,
	@SerializedName("Production") val production : String,
	@SerializedName("Website") val website : String,
	@SerializedName("Response") val response : Boolean
): Parcelable