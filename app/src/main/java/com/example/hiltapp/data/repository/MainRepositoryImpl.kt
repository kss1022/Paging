package com.example.hiltapp.data.repository

import com.example.hiltapp.data.db.dao.BaseDao
import com.example.hiltapp.data.entity.BaseEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val baseDao : BaseDao,
    private val ioDispatchers : CoroutineDispatcher
) : MainRepository {

    override suspend fun getList(): List<BaseEntity> = withContext(ioDispatchers) {
        baseDao.getList()
    }

    override suspend fun insert(entity: BaseEntity) = withContext(ioDispatchers){
        baseDao.insert(entity)
    }
}