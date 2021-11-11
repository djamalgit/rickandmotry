package com.example.rickandmorty.api

class ApiHelper(private val retrofitQuery: RetrofitQuery, private val query: String ) {
    suspend fun getCharacter() = retrofitQuery.getCharacter(query)
}