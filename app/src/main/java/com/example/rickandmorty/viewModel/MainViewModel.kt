package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.rickandmorty.Resource
import com.example.rickandmorty.repository.MainRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun getCharacter() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getCharacter()))
        } catch (ex: Exception) {
            emit(Resource.error(data = null, message = ex.message?:"Error"))
        }
    }
}
