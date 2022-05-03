package com.example.hiltapp.data.repository

import com.example.hiltapp.data.entity.BaseEntity

interface MainRepository {

    suspend fun getList() : List<BaseEntity>

    suspend fun insert( entity: BaseEntity)
}