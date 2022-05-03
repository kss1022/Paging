package com.example.hiltapp.data.repository

import com.example.hiltapp.data.entity.Album
import kotlinx.coroutines.flow.Flow


interface AlbumRepository {

    suspend fun load(): Flow<HashMap<Long?, Album>>
}