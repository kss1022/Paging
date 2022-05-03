package com.example.hiltapp.di

import android.content.Context
import androidx.room.Room
import com.example.hiltapp.data.db.AppDataBase
import com.example.hiltapp.data.db.dao.BaseDao
import com.example.hiltapp.data.entity.SystemUIType
import com.example.hiltapp.data.repository.AlbumRepository
import com.example.hiltapp.data.repository.AlbumRepositoryImpl
import com.example.hiltapp.data.repository.MainRepository
import com.example.hiltapp.data.repository.MainRepositoryImpl
import com.example.hiltapp.util.lifecycle.SingleLiveEvent
import com.example.hiltapp.util.provider.ResourceProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module(includes = [AppModuleBinds::class])
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideIoDispatchers() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase =
        Room.databaseBuilder(
            context, AppDataBase::class.java, AppDataBase.APP_DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideBaseDao(db: AppDataBase): BaseDao = db.getBaseDao()


//    @Singleton
//    @Provides
//    fun providePreferenceManager(@ApplicationContext context: Context) = PreferenceManager(
//        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
//    )


//    @Provides
//    fun provideLinearLayoutManager(@ApplicationContext context: Context): LinearLayoutManager {
//        return LinearLayoutManager(context)
//    }
//
//    @Provides
//    fun provideDividerItemDecoration(@ApplicationContext context: Context): DividerItemDecoration {
//        return DividerItemDecoration(context, DividerItemDecoration.DIVIDER_HORIZONTAL).apply {
//            setDividerMargin(16f)
//            setLastItemDividerVisible(true)
//        }
//    }

    @Singleton
    @Provides
    fun provideSystemUIType(): SingleLiveEvent<SystemUIType> {
        return SingleLiveEvent<SystemUIType>().apply {
            value = SystemUIType.NORMAL
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {
    @Singleton
    @Binds
    abstract fun bindResourceProvider(repository: ResourceProvider): ResourceProvider


    @Singleton
    @Binds
    abstract fun bindMainRepository(repository: MainRepositoryImpl): MainRepository
}