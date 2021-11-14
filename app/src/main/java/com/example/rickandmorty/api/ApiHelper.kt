package com.example.rickandmorty.api

class ApiHelper(private val retrofitQuery: RetrofitQuery) {
    suspend fun getCharacter() = retrofitQuery.getCharacter()
}