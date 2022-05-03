package com.example.hiltapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hiltapp.data.entity.BaseEntity

@Dao
interface BaseDao {


    @Query("SELECT * FROM  BaseEntity")
    suspend fun getList() : List<BaseEntity>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insert( entity: BaseEntity )
}