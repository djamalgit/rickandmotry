package com.example.rickandmorty.repository

import com.example.rickandmorty.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getCharacter() = apiHelper.getCharacter()

}