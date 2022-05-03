package com.example.hiltapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hiltapp.data.db.dao.BaseDao
import com.example.hiltapp.data.entity.BaseEntity

@Database( entities = [ BaseEntity::class] , version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase(){

    abstract fun getBaseDao() : BaseDao


    companion object{
        const val APP_DATABASE_NAME = "HiltApp_AppDataBase"
    }

}