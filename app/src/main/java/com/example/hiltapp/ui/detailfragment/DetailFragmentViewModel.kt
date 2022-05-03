package com.example.hiltapp.ui.detailfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.hiltapp.data.entity.MediaItem
import com.example.hiltapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailFragmentViewModel @Inject constructor(
    private val app: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {

    val checkBoxEnabled = MutableLiveData(true)
    val isChecked = MutableLiveData<Boolean>()
    var currentMediaItem: MediaItem? = null
    val checkBoxClickEvent = SingleLiveEvent<MediaItem?>()
//    val fullScreen = MutableLiveData<Boolean>(false)

    fun onCheckBoxClick() {
        checkBoxClickEvent.value = currentMediaItem
    }

//    fun crossfade() {
//        fullScreen.value = !(fullScreen.value ?: true)
//    }
}