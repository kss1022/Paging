package com.example.hiltapp.ui

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import com.example.hiltapp.util.PagingConstants
import javax.inject.Inject

class ContentObserver @Inject constructor(
    val context: Context
) : ContentObserver(Handler(Looper.getMainLooper())), DefaultLifecycleObserver {

    private val contentChangedEvent = MutableLiveData<Uri?>()

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        context.contentResolver.unregisterContentObserver(this)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        context.contentResolver.registerContentObserver(
            PagingConstants.getContentUri(),
            true,
            this
        )
    }


    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        contentChangedEvent.value = uri
    }

    fun getContentChangedEvent(): LiveData<Uri?> {
        return contentChangedEvent
    }
}