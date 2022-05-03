package com.example.hiltapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.hiltapp.data.entity.Media
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    fun getItems(
        selectionType: MediaPagingSource.SelectionType,
        bucketId: Long?,
        startPosition: Int,
        pageSize: Int
    ): Flow<PagingData<Media>>

    fun invalidate()


    fun getCount(): LiveData<Int>
}