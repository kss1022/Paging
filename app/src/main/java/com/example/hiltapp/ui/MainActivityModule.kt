package com.example.hiltapp.ui

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped


@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {


    @Provides
    @ActivityScoped
    fun provideContext(@ActivityContext context: Context): Context = context

    @Provides
    @ActivityScoped
    fun provideToolbarViewModel(activity: FragmentActivity): ToolbarViewModel {
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory).get(
            ToolbarViewModel::class.java
        )
    }

    @Provides
    @ActivityScoped
    fun provideOptionMenuViewModel(activity: FragmentActivity): OptionMenuViewModel {
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory).get(
            OptionMenuViewModel::class.java
        )
    }

    @Provides
    @ActivityScoped
    fun provideSharedMenuViewModel(activity: FragmentActivity): MainActivitySharedViewModel {
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory).get(
            MainActivitySharedViewModel::class.java
        )
    }
}