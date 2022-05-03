package com.example.hiltapp.ui.mediafragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MediaFragmentViewModel @Inject constructor(
) : ViewModel() {

    fun fetchData(): Job = viewModelScope.launch {
    }

}