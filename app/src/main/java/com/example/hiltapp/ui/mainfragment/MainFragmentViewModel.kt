package com.example.hiltapp.ui.mainfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiltapp.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun fetchData(): Job = viewModelScope.launch {
    }
}