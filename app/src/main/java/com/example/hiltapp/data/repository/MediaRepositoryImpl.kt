package com.example.hiltapp.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.hiltapp.data.entity.Media
import com.example.hiltapp.util.PagingConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

class MediaRepositoryImpl(
    private val context : Context
) : MediaRepository {

    private var currentPagingSource: MediaPagingSource? = null

    private val count = MutableLiveData<Int>()

    override fun getItems(
        selectionType: MediaPagingSource.SelectionType,
        bucketId: Long?,
        startPosition: Int,
        pageSize: Int
    ): Flow<PagingData<Media>> {

        return Pager(
            PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = true,
                initialLoadSize = PagingConstants.DEFAULT_PAGE_SIZE
            ),
            initialKey = startPosition
        ) {
            MediaPagingSource(
                context = context,
                bucketId = bucketId,
                selectionType = selectionType,
                count = count
            ).also {
                currentPagingSource = it
            }
        }.flow
    }

    override fun invalidate(){
        currentPagingSource?.invalidate()
    }


    override fun getCount(): LiveData<Int> = count
}