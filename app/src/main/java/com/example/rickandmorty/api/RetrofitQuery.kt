package com.example.rickandmorty.api


import com.example.rickandmorty.CharacterBase
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RetrofitQuery {
    @GET("character")
    fun getCharacter(
        @QueryMap
        options: MutableMap<String, String>
    ): Call<CharacterBase>
    @GET("character")
    fun getCharacter(
        @Query("name")
        name: String,
        @Query("status")
        status: String
    ): Call<CharacterBase>
    @GET("character")
   suspend fun getCharacter(
        @Query("name")
        name: String
    ): CharacterBase
}