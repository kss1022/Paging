package com.example.hiltapp.ui.albumfragmnet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.hiltapp.data.entity.AlbumItem
import com.example.hiltapp.data.repository.AlbumRepository
import com.example.hiltapp.data.repository.AlbumRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumFragmentViewModel @Inject constructor(
    private val app: Application,
    private val savedStateHandle: SavedStateHandle
 ) : AndroidViewModel(app) {

    val items = MutableLiveData<List<AlbumItem>>()
    lateinit var  repository : AlbumRepositoryImpl

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.load().map { map ->
                map.values.toList()
                    .sortedByDescending { folder -> folder.order }
                    .map { folder ->
                        AlbumItem(folder)
                    }
            }.collectLatest {
                items.postValue(it)
            }
        }
    }


    fun fetchData() : Job = viewModelScope.launch {  }


}
