package com.example.rickandmorty

import com.google.gson.annotations.SerializedName

data class CharacterBase (

    @SerializedName("info") val info : Info,
    @SerializedName("results") val results : ArrayList<Results>
)

data class Info (

    @SerializedName("count") val count : Int,
    @SerializedName("pages") val pages : Int,
    @SerializedName("next") val next : String,
    @SerializedName("prev") val prev : String
)
data class Results (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("status") val status : String,
    @SerializedName("species") val species : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("image") val image : String
)