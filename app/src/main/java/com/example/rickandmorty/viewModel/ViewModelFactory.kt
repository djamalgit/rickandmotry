package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.api.ApiHelper
import com.example.rickandmorty.repository.MainRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(MainViewModel::class.java))
       {
           return MainViewModel(MainRepository(apiHelper)) as T
       }
        throw IllegalArgumentException("Unknown class name")
    }
}