package com.example.rickandmorty.api


import com.example.rickandmorty.CharacterBase
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitQuery {

    @GET("character")
    fun getCharacter(
        @Query("name")
        name: String,
        @Query("status")
        status: String,
        @Query("page")
        page: Int = 1
    ): Call<CharacterBase>

    @GET("character")
    suspend fun getCharacter(
    ): CharacterBase


}