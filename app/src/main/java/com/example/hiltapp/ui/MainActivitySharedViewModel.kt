package com.example.hiltapp.ui

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.hiltapp.data.entity.Album
import com.example.hiltapp.data.entity.Media
import com.example.hiltapp.data.entity.MediaItem
import com.example.hiltapp.data.repository.MediaPagingSource
import com.example.hiltapp.data.repository.MediaRepository
import com.example.hiltapp.data.repository.MediaRepositoryImpl
import com.example.hiltapp.util.PagingConstants
import com.example.hiltapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class MainActivitySharedViewModel @Inject constructor(
    private val app: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(app),
    DefaultLifecycleObserver {

    val repository: MediaRepository = MediaRepositoryImpl(app)
    val selection: Selection = savedStateHandle.get<Selection>(KEY_SAVED_SELECTION) ?: Selection()
    val currentFolder = savedStateHandle.getLiveData<Album>(KEY_FOLDER)
    val itemCount = repository.getCount()

    val itemClickEvent = SingleLiveEvent<Triple<View, Media, Int>?>()
    val bindingItemAdapterPosition = AtomicInteger(PagingConstants.NO_POSITION)


    companion object {
        const val KEY_FOLDER = "key_folder"
        const val KEY_SAVED_SELECTION = "key_saved_selection"
    }

    init {
        if (!savedStateHandle.contains(KEY_FOLDER)) {
            savedStateHandle.set(KEY_FOLDER, null)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        savedStateHandle.set(KEY_SAVED_SELECTION, selection)
    }


    //flatMapLastest 최신데이터만 이용
    val items: Flow<PagingData<MediaItem>> = savedStateHandle.getLiveData<Album>(KEY_FOLDER)
        .asFlow()
        .flatMapLatest { album: Album? ->
            repository.getItems(
                MediaPagingSource.SelectionType.IMAGE_AND_VIDEO,
                album?.bucketId,
                PagingConstants.DEFAULT_POSITION,
                PagingConstants.DEFAULT_PAGE_SIZE
            ).map { pagingData ->
                pagingData.map { media ->
                    MediaItem(media)
                }
            }
        }
        .cachedIn(viewModelScope)

    fun setBucketId(album: Album?) {
        savedStateHandle.set(KEY_FOLDER, album)
    }

    fun onItemClick(v: View, item: MediaItem, position: Int) {
        itemClickEvent.value = Triple(v, item.media, position)
    }

    fun onCheckBoxClick(item: MediaItem) {
        selection.toggle(item.getId(), item.media)
    }
}